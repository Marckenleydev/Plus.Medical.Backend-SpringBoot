package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.cache.CacheStore;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.entity.*;
import marc.dev.DoctorBooking_appointement.enumeration.Authority;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import marc.dev.DoctorBooking_appointement.event.StaffEvent;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.repository.*;
import marc.dev.DoctorBooking_appointement.service.CloudinaryService;
import marc.dev.DoctorBooking_appointement.service.StaffService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.REGISTRATION;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.RESETPASSWORD;
import static marc.dev.DoctorBooking_appointement.utils.StaffUtils.createStaffEntity;
import static marc.dev.DoctorBooking_appointement.utils.StaffUtils.fromStaffEntity;
import static marc.dev.DoctorBooking_appointement.validation.UserValidation.verifyAccountStatus;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;
    private final CloudinaryService cloudinaryService;


    @Override
    public void createStaff(String firstName, String lastName, String email, String password, MultipartFile StaffImageUrl) {
        var staff = staffRepository.findByEmailIgnoreCase(email);

        if(staff.isPresent()){
            throw new ApiException("Email already exists. Use a different email and try again");
        }
        String imageUrl = cloudinaryService.uploadFile(StaffImageUrl, "Plus-Medical-Images");



        var staffEntity = staffRepository.save(createNewStaff(firstName, lastName, email,imageUrl));
        var credentialEntity = new CredentialEntity(staffEntity, encoder.encode(password));
        credentialRepository.save(credentialEntity);

        var confirmationEntity = new ConfirmationEntity(staffEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new StaffEvent(staffEntity, REGISTRATION, Map.of("key", confirmationEntity.getToken())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyAccount(String key) {
        var confirmationEntity = getStaffConfirmation(key);
        var staffEntity = getStaffEntityByEmail(confirmationEntity.getStaffEntity().getEmail());
        staffEntity.setEnabled(true);
        staffRepository.save(staffEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {

        var staffEntity = getStaffEntityByEmail(email);
        RequestContext.setUserId(staffEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(staffEntity.getEmail()) == null) {
                    staffEntity.setLoginAttempts(0);
                    staffEntity.setAccountNonLocked(true);
                }
                staffEntity.setLoginAttempts(staffEntity.getLoginAttempts() + 1);
                userCache.put(staffEntity.getEmail(), staffEntity.getLoginAttempts());
                if (userCache.get(staffEntity.getEmail()) > 5) {
                    staffEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                staffEntity.setAccountNonLocked(true);
                staffEntity.setLoginAttempts(0);
                staffEntity.setLastLogin(now());
                userCache.evict(staffEntity.getEmail());
            }
        }
        staffRepository.save(staffEntity);
    }

    @Override
    public User getStaffByUserId(String userId) {
        var staffEntity = staffRepository.findStaffByUserId(userId)
                .orElseThrow(() -> new ApiException("Staff not found for userId: " + userId));
        return fromStaffEntity(staffEntity, staffEntity.getRole(), getStaffCredentialById(staffEntity.getId()));
    }


    @Override
    public User getStaffByEmail(String email) {
        StaffEntity staffEntity = getStaffEntityByEmail(email);

        return fromStaffEntity(staffEntity, staffEntity.getRole(), getStaffCredentialById(staffEntity.getId()));
    }

    @Override
    public CredentialEntity getStaffCredentialById(Long id) {
        var credentialById = credentialRepository.getCredentialByStaffEntityId(id);
        return credentialById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    @Override
    public void resetPassword(String email) {
        var staff = getStaffEntityByEmail(email);
        var confirmation = getStaffConfirmation(staff);

        if(confirmation != null) {
            publisher.publishEvent(new StaffEvent(staff, RESETPASSWORD, Map.of("key", confirmation.getToken())));
        }else {
            var confirmationEntity = new ConfirmationEntity(staff);
            confirmationRepository.save(confirmationEntity);
            publisher.publishEvent(new StaffEvent(staff, RESETPASSWORD, Map.of("key", confirmationEntity.getToken())));
        }

    }

    @Override
    public User verifyStaffPasswordKey(String key) {
        var confirmationEntity = getStaffConfirmation(key);
        if(confirmationEntity == null) {
            throw new ApiException("Unable to find token");
        }
        var staffEntity = getStaffEntityByEmail(confirmationEntity.getStaffEntity().getEmail());
        if(staffEntity == null) {
            throw new ApiException("Incorrect Token");
        }
        verifyAccountStatus(staffEntity);
        confirmationRepository.delete(confirmationEntity);
        return fromStaffEntity(staffEntity, staffEntity.getRole(), getStaffCredentialById(staffEntity.getId()));
    }

    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var patient = getStaffByUserId(userId);
        var credential = getStaffCredentialById(patient.getId());
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var staff = getStaffEntityByUserId(userId);
        verifyAccountStatus(staff);

        var credential = getStaffCredentialById(staff.getId());
        if(!encoder.matches(currentPassword, credential.getPassword())){throw new ApiException("Existing passwords is incorrect. Please try again");}
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public User updateStaff(String userId, String firstName, String lastName, String email, String phone, String medical_history) {
        var staffEntity = getStaffEntityByUserId(userId);
        staffEntity.setFirstName(firstName);
        staffEntity.setLastName(lastName);
        staffEntity.setEmail(email);
        staffEntity.setPhone(phone);


        staffRepository.save(staffEntity);

        return fromStaffEntity(staffEntity, staffEntity.getRole(), getStaffCredentialById(staffEntity.getId()));
    }
    @Override
    public void updateRole(String userId, String role) {
        var staffEntity = getStaffEntityByUserId(userId);
        staffEntity.setRole(getRoleName(role));
        staffRepository.save(staffEntity);

    }

    @Override
    public String uploadPhoto(String userId, MultipartFile file) {
        return null;
    }

    @Override
    public User getStaffById(Long id) {
        var staffEntity = staffRepository.findById(id).orElseThrow(()-> new ApiException("User not found"));
        return fromStaffEntity(staffEntity, staffEntity.getRole(), getStaffCredentialById(staffEntity.getId()));
    }

    private StaffEntity getStaffEntityByUserId(String userId) {
        var staffByUserId = staffRepository.findStaffByUserId(userId);
        return staffByUserId.orElseThrow(() -> new ApiException("User not found"));
    }
    private StaffEntity getStaffEntityById(Long id) {
        var staffById = staffRepository.findById(id);
        return staffById.orElseThrow(() -> new ApiException("User not found"));
    }
    private ConfirmationEntity getStaffConfirmation(String key) {
        return confirmationRepository.findByToken(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }
    private ConfirmationEntity getStaffConfirmation(StaffEntity staff) {
        return confirmationRepository.findByStaffEntity(staff).orElse(null);
    }
    private StaffEntity getStaffEntityByEmail(String email) {
        var staffByEmail = staffRepository.findByEmailIgnoreCase(email);
        return staffByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private StaffEntity createNewStaff(String firstName, String lastName, String email, String imageUrl){
        var role = getRoleName(Authority.STAFF.name());
        return createStaffEntity(firstName, lastName, email,imageUrl,  role);
    }
}

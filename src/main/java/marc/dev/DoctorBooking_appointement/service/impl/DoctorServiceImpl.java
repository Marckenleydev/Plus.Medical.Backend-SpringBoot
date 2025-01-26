package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.cache.CacheStore;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;

import marc.dev.DoctorBooking_appointement.dto.Feedback;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.entity.*;
import marc.dev.DoctorBooking_appointement.enumeration.Authority;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import marc.dev.DoctorBooking_appointement.event.DoctorEvent;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.repository.*;
import marc.dev.DoctorBooking_appointement.service.CloudinaryService;
import marc.dev.DoctorBooking_appointement.service.DoctorService;
import marc.dev.DoctorBooking_appointement.utils.FeedbackUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.REGISTRATION;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.RESETPASSWORD;
import static marc.dev.DoctorBooking_appointement.utils.UserUtils.*;
import static marc.dev.DoctorBooking_appointement.validation.UserValidation.verifyAccountStatus;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final FeedbackRepository feedbackRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;
    private final SpecialisationRepository specialisationRepository;
    private final CloudinaryService cloudinaryService;
    public void createDoctor(String firstName, String lastName, String email, String password, String specialisationName,MultipartFile doctorImageUrl) {
        var doctor = doctorRepository.findByEmailIgnoreCase(email);
        if(doctor.isPresent()){
            throw new ApiException("Email already exists. Use a different email and try again");

        }
        String imageUrl = cloudinaryService.uploadFile(doctorImageUrl, "Plus-Medical-Images");
        var doctorEntity = doctorRepository.save(createNewDoctor(firstName, lastName, email, specialisationName, imageUrl));
        var credentialEntity = new CredentialEntity(doctorEntity, encoder.encode(password));
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(doctorEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new DoctorEvent(doctorEntity, REGISTRATION, Map.of("key", confirmationEntity.getToken())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyAccount(String key) {
        var confirmationEntity = getDoctorConfirmation(key);
        var doctorEntity = getDoctorEntityByEmail(confirmationEntity.getDoctorEntity().getEmail());
        doctorEntity.setEnabled(true);
        doctorRepository.save(doctorEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var doctorEntity = getDoctorEntityByEmail(email);
        RequestContext.setUserId(doctorEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(doctorEntity .getEmail()) == null) {
                    doctorEntity.setLoginAttempts(0);
                    doctorEntity.setAccountNonLocked(true);
                }
                doctorEntity .setLoginAttempts(doctorEntity.getLoginAttempts() + 1);
                userCache.put(doctorEntity.getEmail(), doctorEntity.getLoginAttempts());
                if (userCache.get(doctorEntity.getEmail()) > 5) {
                    doctorEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                doctorEntity.setAccountNonLocked(true);
                doctorEntity.setLoginAttempts(0);
                doctorEntity.setLastLogin(now());
                userCache.evict(doctorEntity.getEmail());
            }
        }
        doctorRepository.save(doctorEntity);
    }

    @Override
    public User getDoctorByUserId(String userId) {
        var doctorEntity = doctorRepository.findDoctorByUserId(userId).orElseThrow(() -> new ApiException("User not found"));
        return fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
    }

    @Override
    public User getDoctorByEmail(String email) {
        DoctorEntity doctorEntity = getDoctorEntityByEmail(email);

        return fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
    }

    @Override
    public CredentialEntity getDoctorCredentialById(Long id) {
        var credentialById = credentialRepository.getCredentialByDoctorEntityId(id);
        return credentialById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    @Override
    public void resetPassword(String email) {
        var doctor = getDoctorEntityByEmail(email);
        var confirmation = getDoctorConfirmation(doctor);

        if(confirmation != null) {
            publisher.publishEvent(new DoctorEvent(doctor , RESETPASSWORD, Map.of("key", confirmation.getToken())));
        }else {
            var confirmationEntity = new ConfirmationEntity(doctor );
            confirmationRepository.save(confirmationEntity);
            publisher.publishEvent(new DoctorEvent(doctor , RESETPASSWORD, Map.of("key", confirmationEntity.getToken())));
        }
    }



    @Override
    public User verifyDoctorPasswordKey(String key) {
        var confirmationEntity = getDoctorConfirmation(key);
        if(confirmationEntity == null) {
            throw new ApiException("Unable to find token");
        }
        var doctorEntity = getDoctorEntityByEmail(confirmationEntity.getPatientEntity().getEmail());
        if(doctorEntity == null) {
            throw new ApiException("Incorrect Token");
        }
        verifyAccountStatus(doctorEntity);
        confirmationRepository.delete(confirmationEntity);
        return fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
    }

    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var doctor = getDoctorByUserId(userId);
        var credential = getDoctorCredentialById(doctor.getId());
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var doctor = getDoctorEntityByUserId(userId);
        verifyAccountStatus(doctor);

        var credential = getDoctorCredentialById(doctor.getId());
        if(!encoder.matches(currentPassword, credential.getPassword())){throw new ApiException("Existing passwords is incorrect. Please try again");}
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public User updateDoctor(String userId, String firstName, String lastName, String email, String phone) {
        var doctorEntity = getDoctorEntityByUserId(userId);
        doctorEntity.setFirstName(firstName);
        doctorEntity.setLastName(lastName);
        doctorEntity.setEmail(email);
        doctorEntity.setPhone(phone);


        doctorRepository.save(doctorEntity);

        return fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
    }

    @Override
    public void updateRole(String userId, String role) {
        var doctorEntity = getDoctorEntityByUserId(userId);
        doctorEntity.setRole(getRoleName(role));
        doctorRepository.save(doctorEntity);

    }

    @Override
    public Page<IDoctor> getDoctors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
   return doctorRepository.findDoctors(pageable);



    }

    @Override
    public User getDoctorByDoctorId(String doctorId) {
        var doctorEntity = doctorRepository.findDoctorByDoctorId(doctorId).orElseThrow(()-> new ApiException("Could not find doctor"));

        List<Feedback> feedbacks = feedbackRepository.findByDoctorDoctorId(doctorEntity.getDoctorId()).stream()
                .map(FeedbackUtils::fromFeedbackEntity)
                .collect(Collectors.toList());

        User doctorDTO = fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
        doctorDTO.setFeedbacks(feedbacks);

        return doctorDTO;
    }

    @Override
    public Page<IDoctor> getDoctors(int page, int size,String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("first_Name"));
        return doctorRepository.findDoctorsByFirstNameAndOrLastName(name,pageable);



    }

    @Override
    public String uploadPhoto(String userId, MultipartFile file) {
        return null;
    }

    @Override
    public User getDoctorById(Long id) {
        var doctorEntity = doctorRepository.findById(id).orElseThrow(()-> new ApiException("User not found"));
        return fromDoctorEntity(doctorEntity, doctorEntity.getRole(), getDoctorCredentialById(doctorEntity.getId()));
    }

    public SpecialisationEntity getSpecialisationByName(String name) {
        return specialisationRepository.findByName(name).orElseThrow(()->new ApiException("No such specialisation found"));
    }
    private ConfirmationEntity getDoctorConfirmation(String key) {
        return confirmationRepository.findByToken(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }
    private ConfirmationEntity getDoctorConfirmation(DoctorEntity doctor) {
        return confirmationRepository.findByDoctorEntity(doctor).orElse(null);
    }
    private DoctorEntity getDoctorEntityByEmail(String email) {
        var doctorByEmail = doctorRepository.findByEmailIgnoreCase(email);
        return doctorByEmail.orElseThrow(() -> new ApiException("User not found"));
    }
    public DoctorEntity getDoctorEntityByUserId(String userId) {
        var doctorByUserId = doctorRepository.findDoctorByUserId(userId);
        return doctorByUserId.orElseThrow(() -> new ApiException("User not found"));
    }
    private DoctorEntity createNewDoctor(String firstName, String lastName, String email, String specialisationName,String imageUrl){
        var role = getRoleName(Authority.DOCTOR.name());
        var specialisation = getSpecialisationByName(specialisationName);
        return createDoctorEntity(firstName, lastName, email,imageUrl, role,specialisation);
    }
}

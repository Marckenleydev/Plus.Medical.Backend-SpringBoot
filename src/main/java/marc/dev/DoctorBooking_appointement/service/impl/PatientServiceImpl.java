package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.cache.CacheStore;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.dto.api.IPatient;
import marc.dev.DoctorBooking_appointement.entity.ConfirmationEntity;
import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.enumeration.Authority;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import marc.dev.DoctorBooking_appointement.event.PatientEvent;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.repository.ConfirmationRepository;
import marc.dev.DoctorBooking_appointement.repository.CredentialRepository;
import marc.dev.DoctorBooking_appointement.repository.PatientRepository;
import marc.dev.DoctorBooking_appointement.repository.RoleRepository;
import marc.dev.DoctorBooking_appointement.service.PatientService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.REGISTRATION;
import static marc.dev.DoctorBooking_appointement.enumeration.EventType.RESETPASSWORD;
import static marc.dev.DoctorBooking_appointement.utils.UserUtils.createPatientEntity;
import static marc.dev.DoctorBooking_appointement.utils.UserUtils.fromPatientEntity;
import static marc.dev.DoctorBooking_appointement.validation.UserValidation.verifyAccountStatus;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createPatient(String firstName, String lastName, String email, String password) {
        var patient = patientRepository.findByEmailIgnoreCase(email);

        if(patient.isPresent()){
            throw new ApiException("Email already exists. Use a different email and try again");
        }

        var patientEntity = patientRepository.save(createNewPatient(firstName, lastName, email));
        var credentialEntity = new CredentialEntity(patientEntity, encoder.encode(password));
        credentialRepository.save(credentialEntity);

        var confirmationEntity = new ConfirmationEntity(patientEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new PatientEvent(patientEntity, REGISTRATION, Map.of("key", confirmationEntity.getToken())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyAccount(String key) {
        var confirmationEntity = getPatientConfirmation(key);
        var patientEntity = getPatientEntityByEmail(confirmationEntity.getPatientEntity().getEmail());
        patientEntity.setEnabled(true);
        patientRepository.save(patientEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {

        var patientEntity = getPatientEntityByEmail(email);
        RequestContext.setUserId(patientEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(patientEntity.getEmail()) == null) {
                    patientEntity.setLoginAttempts(0);
                    patientEntity.setAccountNonLocked(true);
                }
                patientEntity.setLoginAttempts(patientEntity.getLoginAttempts() + 1);
                userCache.put(patientEntity.getEmail(), patientEntity.getLoginAttempts());
                if (userCache.get(patientEntity.getEmail()) > 5) {
                    patientEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                patientEntity.setAccountNonLocked(true);
                patientEntity.setLoginAttempts(0);
                patientEntity.setLastLogin(now());
                userCache.evict(patientEntity.getEmail());
            }
        }
        patientRepository.save(patientEntity);
    }

    @Override
    public User getPatientByUserId(String userId) {
        var patientEntity = patientRepository.findPatientByUserId(userId)
                .orElseThrow(() -> new ApiException("Patient not found for userId: " + userId));
        return fromPatientEntity(patientEntity, patientEntity.getRole(), getPatientCredentialById(patientEntity.getId()));
    }


    @Override
    public User getPatientByEmail(String email) {
        PatientEntity patientEntity = getPatientEntityByEmail(email);

        return fromPatientEntity(patientEntity, patientEntity.getRole(), getPatientCredentialById(patientEntity.getId()));
    }

    @Override
    public CredentialEntity getPatientCredentialById(Long id) {
        var credentialById = credentialRepository.getCredentialByPatientEntityId(id);
        return credentialById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    @Override
    public void resetPassword(String email) {
        var patient = getPatientEntityByEmail(email);
        var confirmation = getPatientConfirmation(patient);

        if(confirmation != null) {
            publisher.publishEvent(new PatientEvent(patient, RESETPASSWORD, Map.of("key", confirmation.getToken())));
        }else {
            var confirmationEntity = new ConfirmationEntity(patient);
            confirmationRepository.save(confirmationEntity);
            publisher.publishEvent(new PatientEvent(patient, RESETPASSWORD, Map.of("key", confirmationEntity.getToken())));
        }

    }

    @Override
    public User verifyPatientPasswordKey(String key) {
        var confirmationEntity = getPatientConfirmation(key);
        if(confirmationEntity == null) {
            throw new ApiException("Unable to find token");
        }
        var patientEntity = getPatientEntityByEmail(confirmationEntity.getPatientEntity().getEmail());
        if(patientEntity == null) {
            throw new ApiException("Incorrect Token");
        }
        verifyAccountStatus(patientEntity);
        confirmationRepository.delete(confirmationEntity);
        return fromPatientEntity(patientEntity, patientEntity.getRole(), getPatientCredentialById(patientEntity.getId()));
    }

    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var patient = getPatientByUserId(userId);
        var credential = getPatientCredentialById(patient.getId());
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)){throw new ApiException("Password don't match. Please try again");}
        var patient = getPatientEntityByUserId(userId);
        verifyAccountStatus(patient);

        var credential = getPatientCredentialById(patient.getId());
        if(!encoder.matches(currentPassword, credential.getPassword())){throw new ApiException("Existing passwords is incorrect. Please try again");}
        credential.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credential);
    }

    @Override
    public User updatePatient(String userId, String firstName, String lastName, String email, String phone, String medical_history) {
        var patientEntity = getPatientEntityByUserId(userId);
        patientEntity.setFirstName(firstName);
        patientEntity.setLastName(lastName);
        patientEntity.setEmail(email);
        patientEntity.setPhone(phone);
        patientEntity.setMedical_history(medical_history);

        patientRepository.save(patientEntity);

        return fromPatientEntity(patientEntity, patientEntity.getRole(), getPatientCredentialById(patientEntity.getId()));
    }
    @Override
    public void updateRole(String userId, String role) {
        var patientEntity = getPatientEntityByUserId(userId);
        patientEntity.setRole(getRoleName(role));
        patientRepository.save(patientEntity);

    }

    @Override
    public String uploadPhoto(String userId, MultipartFile file) {
        return null;
    }

    @Override
    public User getPatientById(Long id) {
        var patientEntity = patientRepository.findById(id).orElseThrow(()-> new ApiException("User not found"));
        return fromPatientEntity(patientEntity, patientEntity.getRole(), getPatientCredentialById(patientEntity.getId()));
    }

    @Override
    public Page<IPatient> getPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return patientRepository.findPatients(pageable);



    }

    @Override
    public Page<IPatient> getPatients(int page, int size,String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("first_Name"));
        return patientRepository.findPatientsByFirstNameAndOrLastName(name,pageable);



    }

    public PatientEntity getPatientEntityByUserId(String userId) {
        var patientByUserId = patientRepository.findPatientByUserId(userId);
        return patientByUserId.orElseThrow(() -> new ApiException("User not found"));
    }
    private PatientEntity getPatientEntityById(Long id) {
        var patientById = patientRepository.findById(id);
        return patientById.orElseThrow(() -> new ApiException("User not found"));
    }
    private ConfirmationEntity getPatientConfirmation(String key) {
        return confirmationRepository.findByToken(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }
    private ConfirmationEntity getPatientConfirmation(PatientEntity patient) {
        return confirmationRepository.findByPatientEntity(patient).orElse(null);
    }
    private PatientEntity getPatientEntityByEmail(String email) {
        var patientByEmail = patientRepository.findByEmailIgnoreCase(email);
        return patientByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private PatientEntity createNewPatient(String firstName, String lastName, String email){
        var role = getRoleName(Authority.PATIENT.name());
        return createPatientEntity(firstName, lastName, email, role);
    }
}

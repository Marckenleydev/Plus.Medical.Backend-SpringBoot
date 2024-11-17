package marc.dev.DoctorBooking_appointement.service;


import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.dto.api.IPatient;
import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PatientService {
    void createPatient(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccount(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getPatientByUserId(String userId);
    User getPatientByEmail(String email);
    CredentialEntity getPatientCredentialById(Long id);

    void resetPassword(String email);
    User verifyPatientPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword);
    void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword);
    User updatePatient(String userId, String firstName, String lastName, String email, String phone, String medical_history);
    void updateRole(String userId, String role);

    Page<IPatient> getPatients(int page, int size, String name);
    Page<IPatient> getPatients(int page, int size);


    String uploadPhoto(String userId, MultipartFile file);
    User getPatientById(Long id);
}

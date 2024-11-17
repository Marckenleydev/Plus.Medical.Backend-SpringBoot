package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import org.springframework.web.multipart.MultipartFile;

public interface StaffService {
    void createStaff(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccount(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getStaffByUserId(String userId);
    User getStaffByEmail(String email);
    CredentialEntity getStaffCredentialById(Long id);

    void resetPassword(String email);
    User verifyStaffPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword);
    void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword);
    User updateStaff(String userId, String firstName, String lastName, String email, String phone, String medical_history);
    void updateRole(String userId, String role);




    String uploadPhoto(String userId, MultipartFile file);
    User getStaffById(Long id);
}

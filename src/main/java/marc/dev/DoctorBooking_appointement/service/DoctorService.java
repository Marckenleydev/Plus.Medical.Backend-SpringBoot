package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.dto.Doctor;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DoctorService {
    void createDoctor(String firstName, String lastName, String email, String password,String specialisationName, MultipartFile imageUrl);
    RoleEntity getRoleName(String name);
    void verifyAccount(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getDoctorByUserId(String userId);
    User getDoctorByEmail(String email);
    CredentialEntity getDoctorCredentialById(Long id);

    void resetPassword(String email);

    User verifyDoctorPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword);
    void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword);
    User updateDoctor(String userId, String firstName, String lastName, String email, String phone);
    void updateRole(String userId, String role);

    Page<IDoctor> getDoctors(int page, int size, String name);
    Page<IDoctor> getDoctors(int page, int size);

    User getDoctorByDoctorId(String doctorId);


    String uploadPhoto(String userId, MultipartFile file);
    User getDoctorById(Long id);

}

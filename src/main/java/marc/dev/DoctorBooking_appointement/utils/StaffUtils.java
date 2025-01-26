package marc.dev.DoctorBooking_appointement.utils;

import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.entity.StaffEntity;
import org.springframework.beans.BeanUtils;

import java.util.UUID;
import static marc.dev.DoctorBooking_appointement.constant.Constants.NINETY_DAYS;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class StaffUtils {
    public static StaffEntity createStaffEntity(String firstName, String lastName, String email,String imageUrl, RoleEntity role) {

        return StaffEntity.builder()
                .userId(UUID.randomUUID().toString())
                .staffId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .phone(EMPTY)
                .imageUrl(imageUrl)
                .role(role)
                .build();

    }


    public static User fromStaffEntity(StaffEntity staffEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User staff = new User();
        BeanUtils.copyProperties(staffEntity, staff);
        staff.setLastLogin(staffEntity.getLastLogin().toString());
        staff.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        staff.setCreatedAt(staffEntity.getCreatedAt().toString());
        staff.setUpdatedAt(staffEntity.getUpdatedAt().toString());
        staff.setRole(role.getName());
        staff.setAuthorities(role.getAuthorities().getValue());
        return staff;
    }



    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(now());
    }
}

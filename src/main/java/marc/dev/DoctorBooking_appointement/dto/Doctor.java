package marc.dev.DoctorBooking_appointement.dto;

import lombok.Data;
import marc.dev.DoctorBooking_appointement.entity.SpecialisationEntity;

@Data
public class Doctor {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String doctorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;
    private SpecialisationEntity specialisation;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}

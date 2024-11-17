package marc.dev.DoctorBooking_appointement.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import marc.dev.DoctorBooking_appointement.entity.AppointmentEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IPatient {
    Long getId();
    void setId(Long id);

    @JsonProperty("userId")
    String getUser_Id();
    void setUser_Id(String userId);
    @JsonProperty("doctorId")
    String getDoctor_Id();
    void setDoctor_Id(String doctorId);
    @JsonProperty("referenceId")
    String getReference_Id();
    void getReference_Id(String referenceId);

    String getFirstName();
    void setFirstName(String firstName);

    String getLastName();
    void setLastName(String lastName);

    String getEmail();
    void setEmail(String email);

    String getPhone();
    void setPhone(String phone);

    String getLastLogin();
    void setLastLogin(String lastLogin);

    @JsonProperty("createdAt")
    LocalDateTime getCreated_At();
    void setCreated_At(LocalDateTime createdAt);

    @JsonProperty("updatedAt")
    LocalDateTime getUpdated_At();
    void setUpdated_At(LocalDateTime updatedAt);
    String getImage_Url();
    void setImage_Url(String imageUrl);
    String getRole_Name();
    void setRole_Name(String role);

    String getSpecialisation();
    void setSpecialisation(String speciality);

    String getRole_Authorities();
    void setRole_Authorities(String authorities);

    boolean isAccountNonExpired();
    void setAccountNonExpired(boolean accountNonExpired);

    boolean isAccountNonLocked();
    void setAccountNonLocked(boolean accountNonLocked);


    boolean isEnabled();
    void setEnabled(boolean enabled);

    List<AppointmentEntity> getAppointments();
    void setAppointments(List<AppointmentEntity> appointments);
}

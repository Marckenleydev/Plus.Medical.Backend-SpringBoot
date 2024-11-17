package marc.dev.DoctorBooking_appointement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Specialisation {
    private Long id;
    private String name;
    private String createdAt;
    private String updatedAt;
    private String createdByName;
    private String createdByEmail;
    private String createdByPhone;
    private String createdByLastLogin;
    private String updatedByName;
}

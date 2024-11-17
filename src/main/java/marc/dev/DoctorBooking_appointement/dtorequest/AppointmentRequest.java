package marc.dev.DoctorBooking_appointement.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentRequest {
    @NotEmpty(message = "Doctor ReferenceId cannot be empty")
    private String userId;
}

package marc.dev.DoctorBooking_appointement.dtorequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ServiceRequest {
    @NotEmpty(message = "Title cannot be empty or null")
    private String title;
    @NotEmpty(message = "Description cannot be empty or null")
    private String description;
}

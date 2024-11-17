package marc.dev.DoctorBooking_appointement.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackRequest {
    @NotEmpty(message = "Message cannot be empty or null")
    private String message;
}

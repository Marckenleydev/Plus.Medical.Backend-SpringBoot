package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
@JsonInclude(NON_DEFAULT)
public class MessageEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String feedbackId;
    private String email;
    private String subject;
    private String message;
}

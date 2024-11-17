package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import marc.dev.DoctorBooking_appointement.enumeration.Status;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
@JsonInclude(NON_DEFAULT)
public class AppointmentEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String appointmentId;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "appointment_doctors",
            joinColumns = @JoinColumn(
                    name = "appointment_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"
            )
    )
    private DoctorEntity doctor;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "appointment_patients",
            joinColumns = @JoinColumn(
                    name = "appointment_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "patient_id", referencedColumnName = "id"
            )
    )
    private PatientEntity patientEntity;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDateTime;
    private Status status;
}

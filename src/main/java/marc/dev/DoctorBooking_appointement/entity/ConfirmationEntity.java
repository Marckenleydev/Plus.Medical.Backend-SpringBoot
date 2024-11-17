package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirmations")
@JsonInclude(NON_DEFAULT)
public class ConfirmationEntity extends Auditable{
    private String token;
    @OneToOne(targetEntity = PatientEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @OnDelete(action = CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonProperty("patient_id")
    private PatientEntity patientEntity;

    @OneToOne(targetEntity = DoctorEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    @OnDelete(action = CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonProperty("doctor_id")
    private DoctorEntity doctorEntity;

    @OneToOne(targetEntity = StaffEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    @OnDelete(action = CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonProperty("staff_id")
    private StaffEntity staffEntity;

    public ConfirmationEntity(PatientEntity patientEntity ){
        this.patientEntity = patientEntity;
        this.token = UUID.randomUUID().toString();
    }

    public ConfirmationEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
        this.token = UUID.randomUUID().toString();
    }

    public ConfirmationEntity(StaffEntity staffEntity) {
        this.staffEntity = staffEntity;
        this.token = UUID.randomUUID().toString();
    }


}

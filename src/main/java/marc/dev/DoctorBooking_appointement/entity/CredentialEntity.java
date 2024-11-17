package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials")
@JsonInclude(NON_DEFAULT)
public class CredentialEntity extends  Auditable{
    private String password;
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

    public CredentialEntity(PatientEntity patientEntity, String password){
        this.patientEntity =  patientEntity;
        this.password = password;
    }
    public CredentialEntity(DoctorEntity doctorEntity, String password){
        this.doctorEntity =  doctorEntity;
        this.password = password;
    }

    public CredentialEntity(StaffEntity staffEntity, String password){
        this.staffEntity=  staffEntity;
        this.password = password;
    }
}

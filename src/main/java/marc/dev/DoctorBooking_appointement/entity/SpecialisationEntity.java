package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specialisations")
@JsonInclude(NON_DEFAULT)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class SpecialisationEntity extends Auditable{
    private String name;
}

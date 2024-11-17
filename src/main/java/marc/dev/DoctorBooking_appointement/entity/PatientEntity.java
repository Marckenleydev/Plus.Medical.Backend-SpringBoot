package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
@JsonInclude(NON_DEFAULT)
public class PatientEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String userId;
    @Column(updatable = false, unique = true, nullable = false)
    private String patientId;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private String phone;
    private String imageUrl;
    private String medical_history;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<FeedbackEntity> feedbacks = new ArrayList<>();
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "patient_roles",
            joinColumns = @JoinColumn(
                    name = "patient_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private RoleEntity role;
}

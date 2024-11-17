package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;
import static marc.dev.DoctorBooking_appointement.constant.Constants.MAX_ABOUT_ME_LENGTH;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
@JsonInclude(NON_DEFAULT)
public class DoctorEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String userId;
    @Column(updatable = false, unique = true, nullable = false)
    private String doctorId;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private String education;
    private String phone;
    @Column(length = 4000)
    private String aboutMe;
    private int experience;
    private Long totalPatients;
    private float rating;
    private int totalRating;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<FeedbackEntity> feedbacks = new ArrayList<>();
    private String imageUrl;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "doctor_specialisations",
            joinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name="specialisation_id", referencedColumnName = "id"
            )
    )
    private SpecialisationEntity specialisation;

    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "doctor_roles",
            joinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private RoleEntity role;


}

package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staffs")
@JsonInclude(NON_DEFAULT)
public class StaffEntity extends Auditable{
    @Column(updatable = false, unique = true, nullable = false)
    private String userId;
    @Column(updatable = false, unique = true, nullable = false)
    private String staffId;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private String phone;
    private String imageUrl;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    @ManyToOne(fetch = EAGER)
    @JoinTable(
            name = "staff_roles",
            joinColumns = @JoinColumn(
                    name = "staff_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private RoleEntity role;
}

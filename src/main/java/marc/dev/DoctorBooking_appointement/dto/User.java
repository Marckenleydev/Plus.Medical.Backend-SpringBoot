package marc.dev.DoctorBooking_appointement.dto;

import lombok.Data;
import marc.dev.DoctorBooking_appointement.entity.FeedbackEntity;
import marc.dev.DoctorBooking_appointement.entity.SpecialisationEntity;

import java.util.List;

@Data
public class User {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String patientId;
    private String referenceId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String aboutMe;
    private int experience;
    private String education;
    private Long totalPatients;
    private float rating;
    private int totalRating;
    private String imageUrl;
    private SpecialisationEntity specialisation;
    private List<Feedback> feedbacks;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;
    private String doctorId;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}

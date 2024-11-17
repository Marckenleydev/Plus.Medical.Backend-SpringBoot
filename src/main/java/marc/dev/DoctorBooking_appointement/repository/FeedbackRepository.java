package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> findByDoctorDoctorId(String doctorId);
}

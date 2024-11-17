package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}

package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByServiceId(String name);
}

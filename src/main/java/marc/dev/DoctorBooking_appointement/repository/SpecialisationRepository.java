package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.entity.SpecialisationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialisationRepository extends JpaRepository<SpecialisationEntity, Long> {
    Optional<SpecialisationEntity> findByReferenceId(String referenceId);
    Optional<SpecialisationEntity> findByName(String name);
}

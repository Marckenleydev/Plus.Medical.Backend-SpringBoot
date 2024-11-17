package marc.dev.DoctorBooking_appointement.repository;


import marc.dev.DoctorBooking_appointement.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
    Optional<CredentialEntity> getCredentialByPatientEntityId(Long userId);
    Optional<CredentialEntity> getCredentialByDoctorEntityId(Long userId);
    Optional<CredentialEntity> getCredentialByStaffEntityId(Long userId);
}

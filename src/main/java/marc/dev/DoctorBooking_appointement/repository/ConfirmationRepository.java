package marc.dev.DoctorBooking_appointement.repository;


import marc.dev.DoctorBooking_appointement.entity.ConfirmationEntity;
import marc.dev.DoctorBooking_appointement.entity.DoctorEntity;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import marc.dev.DoctorBooking_appointement.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findByToken(String token);
    Optional<ConfirmationEntity> findByPatientEntity(PatientEntity patientEntity);
    Optional<ConfirmationEntity> findByDoctorEntity(DoctorEntity doctorEntity);
    Optional<ConfirmationEntity> findByStaffEntity(StaffEntity staffEntity);
}
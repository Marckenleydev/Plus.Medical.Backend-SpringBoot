package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.dto.api.IPatient;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static marc.dev.DoctorBooking_appointement.constant.Constants.*;
import static marc.dev.DoctorBooking_appointement.constant.Constants.SELECT_DOCTORS_BY_NAME_QUERY;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByEmailIgnoreCase(String email);
    Optional<PatientEntity> findPatientByUserId(String userId);
    Optional<PatientEntity> findPatientByPatientId(String patientId);

    @Query(countQuery = SELECT_COUNT_PATIENTS_QUERY, value = SELECT_PATIENTS_QUERY, nativeQuery = true)
    Page<IPatient> findPatients(Pageable pageable);

    @Query(countQuery = SELECT_COUNT_PATIENTS_BY_NAME_QUERY, value = SELECT_PATIENTS_BY_NAME_QUERY, nativeQuery = true)
    Page<IPatient> findPatientsByFirstNameAndOrLastName(@Param("name") String name, Pageable pageable);
}

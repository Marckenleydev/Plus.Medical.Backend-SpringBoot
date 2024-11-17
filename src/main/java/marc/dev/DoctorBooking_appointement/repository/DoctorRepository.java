package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.dto.api.IDoctor;
import marc.dev.DoctorBooking_appointement.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static marc.dev.DoctorBooking_appointement.constant.Constants.*;

public interface DoctorRepository extends JpaRepository<DoctorEntity , Long> {
    Optional<DoctorEntity> findByEmailIgnoreCase(String email);
    Optional<DoctorEntity> findDoctorByUserId(String userId);

    @Query(countQuery = SELECT_COUNT_DOCTORS_QUERY, value = SELECT_DOCTORS_QUERY, nativeQuery = true)
    Page<IDoctor> findDoctors(Pageable pageable);

    @Query(countQuery = SELECT_COUNT_DOCTORS_BY_NAME_QUERY, value = SELECT_DOCTORS_BY_NAME_QUERY, nativeQuery = true)
    Page<IDoctor> findDoctorsByFirstNameAndOrLastName(@Param("name") String name, Pageable pageable);

    Optional<DoctorEntity> findDoctorByDoctorId(String doctorId);
}

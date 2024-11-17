package marc.dev.DoctorBooking_appointement.repository;

import marc.dev.DoctorBooking_appointement.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    Optional<AppointmentEntity> findByAppointmentId(String appointmentId);

    Page<AppointmentEntity> findByPatientEntity(String patientId, Pageable pageable);
    Page<AppointmentEntity>findByDoctorUserId(String doctorUserId, Pageable pageable);
    Page<AppointmentEntity>findByPatientEntityUserId(String patientUserId, Pageable pageable);
}

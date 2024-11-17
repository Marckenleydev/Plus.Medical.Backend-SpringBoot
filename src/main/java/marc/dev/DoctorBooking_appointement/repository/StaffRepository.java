package marc.dev.DoctorBooking_appointement.repository;


import marc.dev.DoctorBooking_appointement.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffEntity,Long> {
    Optional<StaffEntity> findByEmailIgnoreCase(String email);
    Optional<StaffEntity> findStaffByUserId(String id);
}

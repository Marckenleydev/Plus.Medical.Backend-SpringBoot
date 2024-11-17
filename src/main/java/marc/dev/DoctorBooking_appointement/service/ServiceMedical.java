package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.entity.ServiceEntity;
import org.springframework.data.domain.Page;


public interface ServiceMedical {
    void createMedicalService(String title, String description);
    Page<ServiceEntity> getMedicalServices(int page, int size);
    ServiceEntity getMedicalService(String serviceId);
    void updateMedicalService(String id, String title, String description);
}

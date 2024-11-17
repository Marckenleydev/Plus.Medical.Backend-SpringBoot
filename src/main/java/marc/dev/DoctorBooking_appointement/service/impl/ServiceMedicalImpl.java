package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.entity.ServiceEntity;
import marc.dev.DoctorBooking_appointement.repository.ServiceRepository;
import marc.dev.DoctorBooking_appointement.service.ServiceMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static marc.dev.DoctorBooking_appointement.utils.ServiceUtils.createMedicalServiceEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class ServiceMedicalImpl implements ServiceMedical {
    private final ServiceRepository serviceRepository;
    @Override
    public void createMedicalService(String title, String description) {
      serviceRepository.save(createMedicalServiceEntity(title, description));
    }

    @Override
    public Page<ServiceEntity> getMedicalServices(int page, int size) {
        return serviceRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public ServiceEntity getMedicalService(String id) {
        var optionalServiceEntity = serviceRepository.findByServiceId(id);
        return optionalServiceEntity.orElse(null);
    }

    @Override
    public void updateMedicalService(String id, String title, String description) {
       var optionalServiceEntity = serviceRepository.findByServiceId(id);
        if (optionalServiceEntity.isPresent()) {
            ServiceEntity serviceEntity = optionalServiceEntity.get();
            serviceEntity.setTitle(title);
            serviceEntity.setDescription(description);
            serviceRepository.save(serviceEntity);
        }
    }
}

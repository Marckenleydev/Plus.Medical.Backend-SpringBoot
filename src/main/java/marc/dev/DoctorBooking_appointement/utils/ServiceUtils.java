package marc.dev.DoctorBooking_appointement.utils;
import marc.dev.DoctorBooking_appointement.entity.ServiceEntity;


import java.util.UUID;



public class ServiceUtils {
    public static ServiceEntity createMedicalServiceEntity(String title, String description) {

        return ServiceEntity.builder()
                .serviceId(UUID.randomUUID().toString())
                .title(title)
                .description(description)
                .build();

    }
}

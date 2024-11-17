package marc.dev.DoctorBooking_appointement.utils;

import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dto.Specialisation;
import marc.dev.DoctorBooking_appointement.entity.SpecialisationEntity;
import org.springframework.beans.BeanUtils;

public class SpecialisationUtils {

    public static Specialisation fromSpecialisationEntity(SpecialisationEntity specialisationEntity) {
        Specialisation specialisation = new Specialisation();
        BeanUtils.copyProperties(specialisationEntity, specialisation);
        specialisation.setCreatedAt(specialisationEntity.getCreatedAt().toString());
        specialisation.setUpdatedAt(specialisationEntity.getUpdatedAt().toString());
        return specialisation;
    }
}

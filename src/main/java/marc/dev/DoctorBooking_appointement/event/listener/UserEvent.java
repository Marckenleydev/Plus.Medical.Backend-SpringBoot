package marc.dev.DoctorBooking_appointement.event.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import marc.dev.DoctorBooking_appointement.enumeration.EventType;


import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private PatientEntity patient;
    private EventType type;
    private Map<?,?> data;
}

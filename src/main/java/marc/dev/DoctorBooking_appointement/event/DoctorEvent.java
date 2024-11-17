package marc.dev.DoctorBooking_appointement.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import marc.dev.DoctorBooking_appointement.entity.DoctorEntity;
import marc.dev.DoctorBooking_appointement.enumeration.EventType;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class DoctorEvent {
    private DoctorEntity doctor;
    private EventType type;
    private Map<?,?> data;
}

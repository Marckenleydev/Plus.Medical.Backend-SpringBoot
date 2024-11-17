package marc.dev.DoctorBooking_appointement.event.listener;

import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.event.DoctorEvent;
import marc.dev.DoctorBooking_appointement.event.PatientEvent;
import marc.dev.DoctorBooking_appointement.event.StaffEvent;
import marc.dev.DoctorBooking_appointement.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @EventListener
    public void onStaffEvent(StaffEvent event){
        switch (event.getType()){
            case REGISTRATION -> emailService.sendStaffNewAccountEmail(event.getStaff().getFirstName(), event.getStaff().getEmail(), (String)event.getData().get("key"));
            case RESETPASSWORD -> emailService.sendStaffPasswordResetEmail(event.getStaff().getFirstName(), event.getStaff().getEmail(), (String)event.getData().get("key"));
            default -> {}
        }
    }
    @EventListener
    public void onPatientEventListener(PatientEvent event){
        switch (event.getType()){
            case REGISTRATION -> emailService.sendPatientNewAccountEmail(event.getPatient().getFirstName(), event.getPatient().getEmail(), (String)event.getData().get("key"));
            case RESETPASSWORD -> emailService.sendPatientPasswordResetEmail(event.getPatient().getFirstName(), event.getPatient().getEmail(), (String)event.getData().get("key"));
            default -> {}
        }
    }

    @EventListener
    public void onDoctorEventListener(DoctorEvent event){
        switch (event.getType()){
            case REGISTRATION -> emailService.sendDoctorNewAccountEmail(event.getDoctor().getFirstName(), event.getDoctor().getEmail(), (String)event.getData().get("key"));
            case RESETPASSWORD -> emailService.sendDoctorPasswordResetEmail(event.getDoctor().getFirstName(), event.getDoctor().getEmail(), (String)event.getData().get("key"));
            default -> {}
        }
    }
}

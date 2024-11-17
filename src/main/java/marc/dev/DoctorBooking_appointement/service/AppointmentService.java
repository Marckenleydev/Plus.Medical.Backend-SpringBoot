package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.dto.Appointment;
import marc.dev.DoctorBooking_appointement.dto.api.IAppointment;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;


public interface AppointmentService {
    void createAppointment(String patientByUserId, String doctorByUserId, LocalDateTime appointmentDateTime);
    Page<Appointment> getDoctorAppointments(String doctorId, int page, int size);
    Page<Appointment> getPatientAppointments(String patientId, int page, int size);
    Page<Appointment> getAppointments(int page, int size);


    IAppointment getAppointmentByAppointmentId(String doctorId);

}

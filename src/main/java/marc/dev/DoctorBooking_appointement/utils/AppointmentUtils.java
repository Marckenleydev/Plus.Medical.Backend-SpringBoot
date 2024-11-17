package marc.dev.DoctorBooking_appointement.utils;

import marc.dev.DoctorBooking_appointement.dto.Appointment;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.entity.*;
import marc.dev.DoctorBooking_appointement.enumeration.Status;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentUtils {

    public static AppointmentEntity createNewAppointment(LocalDateTime appointmentDateTime, Status status, DoctorEntity doctor, PatientEntity patient) {
        return AppointmentEntity.builder()
                .appointmentId(UUID.randomUUID().toString())
                .appointmentDateTime(appointmentDateTime)
                .status(status)
                .doctor(doctor)
                .patientEntity(patient)
                .build();
    }

    public static Appointment FromAppointmentEntity(AppointmentEntity appointmentEntity) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentEntity, appointment);
        appointment.setAppointmentId(appointment.getAppointmentId());
        appointment.setDoctorName(appointmentEntity.getDoctor().getFirstName() + " " + appointmentEntity.getDoctor().getLastName());
        appointment.setDoctorEmail(appointmentEntity.getDoctor().getEmail());
        appointment.setDoctorPhone(appointmentEntity.getDoctor().getPhone());
        appointment.setPatientName(appointmentEntity.getPatientEntity().getFirstName() + " " + appointmentEntity.getPatientEntity().getLastName());
        appointment.setPatientEmail(appointmentEntity.getPatientEntity().getEmail());
        appointment.setPatientPhone(appointmentEntity.getPatientEntity().getPhone());
        appointment.setAppointmentDateTime(appointmentEntity.getAppointmentDateTime());

        return appointment;
    }
}

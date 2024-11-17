package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.dto.Appointment;
import marc.dev.DoctorBooking_appointement.dto.api.IAppointment;
import marc.dev.DoctorBooking_appointement.entity.AppointmentEntity;
import marc.dev.DoctorBooking_appointement.enumeration.Status;
import marc.dev.DoctorBooking_appointement.repository.AppointmentRepository;

import marc.dev.DoctorBooking_appointement.service.AppointmentService;
import marc.dev.DoctorBooking_appointement.service.EmailService;
import marc.dev.DoctorBooking_appointement.utils.AppointmentUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static marc.dev.DoctorBooking_appointement.utils.AppointmentUtils.*;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final PatientServiceImpl patientService;
    private final DoctorServiceImpl doctorService;
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;
    @Override
    public void createAppointment(String patientUserId, String doctorUserId, LocalDateTime appointmentDateTime) {
        var patientEntity = patientService.getPatientEntityByUserId(patientUserId);
        var doctorEntity = doctorService.getDoctorEntityByUserId(doctorUserId);

        var appointment = createNewAppointment(appointmentDateTime, Status.CONFIRMED, doctorEntity, patientEntity);

        // Save the appointment
        appointmentRepository.save(appointment);
        emailService.sendHtmlConfirmationAppointmentMail(patientEntity.getFirstName(),
                doctorEntity.getLastName(),
                appointmentDateTime,
                appointment.getReferenceId(),
                patientEntity.getEmail());


    }

//    @Override
//    public Page<Appointment> getAppointments(int page, int size, String name) {
//        return null;
//    }

    @Override
    public Page<Appointment> getDoctorAppointments(String doctorUserId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointmentEntities = appointmentRepository.findByDoctorUserId(doctorUserId, pageable);
        List<Appointment> appointments = appointmentEntities.stream()
                .map(AppointmentUtils::FromAppointmentEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(appointments, pageable, appointmentEntities.getTotalElements());
    }

    @Override
    public Page<Appointment> getPatientAppointments(String patientUserId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointmentEntities = appointmentRepository.findByPatientEntityUserId(patientUserId, pageable);
        List<Appointment> appointments = appointmentEntities.stream()
                .map(AppointmentUtils::FromAppointmentEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(appointments, pageable, appointmentEntities.getTotalElements());
    }

    @Override
    public Page<Appointment> getAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointmentEntities = appointmentRepository.findAll(pageable);

        List<Appointment> appointments = appointmentEntities.stream()
                .map(AppointmentUtils::FromAppointmentEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(appointments, pageable, appointmentEntities.getTotalElements());
    }


    @Override
    public IAppointment getAppointmentByAppointmentId(String doctorId) {
        return null;
    }
}

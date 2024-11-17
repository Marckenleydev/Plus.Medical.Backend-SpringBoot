package marc.dev.DoctorBooking_appointement.service;


import java.time.LocalDateTime;

public interface EmailService {
//    void sendNewAccountEmail(String name, String email, String token);
//    void sendPasswordResetEmail(String name, String email, String token);
void sendHtmlConfirmationAppointmentMail(String patientName,
                  String doctorName,
                                         LocalDateTime appointmentDateTime,
                  String AppointmentCode, String to);
    void sendPatientNewAccountEmail(String name, String email, String token);
    void sendPatientPasswordResetEmail(String name, String email, String token);

    void sendDoctorNewAccountEmail(String name, String email, String token);
    void sendDoctorPasswordResetEmail(String name, String email, String token);

    void sendStaffNewAccountEmail(String name, String email, String token);
    void sendStaffPasswordResetEmail(String name, String email, String token);
}
package marc.dev.DoctorBooking_appointement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Appointment {
    private Long id;
    private String appointmentId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String doctorName;
    private String doctorEmail;
    private String doctorPhone;
    private LocalDateTime appointmentDateTime;
}

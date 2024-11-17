package marc.dev.DoctorBooking_appointement.dto;

import lombok.Data;

@Data
public class Feedback {
    private Long id;
    private String referenceId;
    private  String message;
    private  String patientName;
    private  String patientEmail;
    private  String patientPhone;
    private  String patient_ImageUrl;

    private  String doctorName;
    private  String doctorEmail;
    private  String doctorPhone;
    private  String doctor_ImageUrl;

    private String createdAt;
    private String updatedAt;
}

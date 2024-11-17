package marc.dev.DoctorBooking_appointement.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public interface IAppointment {
    Long getId();
    void setId(Long id);
    @JsonProperty("referenceId")
    String getReference_Id();
    void setReference_Id(String referenceId);
    @JsonProperty("AppointmentId")
    String getAppointment_Id();
    void setAppointment_Id(String Appointment);
    @JsonProperty("patientName")
    String getPatient_Name();
    void setPatient_Name(String patientName);
    @JsonProperty("patientEmail")
    String getPatient_Email();
    void setPatient_Email(String patientEmail);
    @JsonProperty("patientPhone")
     String getPatient_Phone();
    void setPatient_Phone(String patientPhone);
    @JsonProperty("doctorName")
    String getDoctor_Name();
    void setDoctor_Name(String doctorName);
    @JsonProperty("doctorEmail")
     String getDoctor_Email();
    void setDoctor_Email(String doctorEmail);
    @JsonProperty("doctorPhone")
     String getDoctor_Phone();
    void setDoctor_Phone(String doctorPhone);

    @JsonProperty("createdAt")
    LocalDateTime getCreated_At();
    void setCreated_At(LocalDateTime createdAt);
    @JsonProperty("updatedAt")
    LocalDateTime getUpdated_At();
    void setUpdated_At(LocalDateTime updatedAt);
}

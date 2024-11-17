package marc.dev.DoctorBooking_appointement.utils;

import marc.dev.DoctorBooking_appointement.dto.Feedback;
import marc.dev.DoctorBooking_appointement.entity.DoctorEntity;
import marc.dev.DoctorBooking_appointement.entity.FeedbackEntity;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import org.springframework.beans.BeanUtils;


public class FeedbackUtils {

    public static Feedback fromFeedbackEntity(FeedbackEntity feedbackEntity) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackEntity, feedback);
        feedback.setPatientName(feedbackEntity.getPatient().getFirstName() + " " + feedbackEntity.getPatient().getLastName());
        feedback.setPatientEmail(feedbackEntity.getPatient().getEmail());
        feedback.setPatientPhone(feedbackEntity.getPatient().getPhone());
        feedback.setPatient_ImageUrl(feedbackEntity.getPatient().getImageUrl());
        feedback.setDoctorName(feedbackEntity.getDoctor().getFirstName() + " " + feedbackEntity.getDoctor().getLastName());
        feedback.setDoctorEmail(feedbackEntity.getDoctor().getEmail());
        feedback.setDoctorPhone(feedbackEntity.getDoctor().getPhone());
        feedback.setDoctor_ImageUrl(feedbackEntity.getDoctor().getImageUrl());
        feedback.setCreatedAt(feedbackEntity.getCreatedAt().toString());
        feedback.setUpdatedAt(feedbackEntity.getUpdatedAt().toString());
        return feedback;
    }
    public static FeedbackEntity createFeedbackEntity(String message, DoctorEntity doctor, PatientEntity patient) {
        return FeedbackEntity.builder()
                .message(message)
                .doctor(doctor)
                .patient(patient)
                .build();
    }
}

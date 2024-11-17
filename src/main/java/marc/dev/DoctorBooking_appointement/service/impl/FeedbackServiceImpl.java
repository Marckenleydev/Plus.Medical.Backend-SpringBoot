package marc.dev.DoctorBooking_appointement.service.impl;

import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.repository.DoctorRepository;
import marc.dev.DoctorBooking_appointement.repository.FeedbackRepository;
import marc.dev.DoctorBooking_appointement.repository.PatientRepository;
import marc.dev.DoctorBooking_appointement.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static marc.dev.DoctorBooking_appointement.utils.FeedbackUtils.createFeedbackEntity;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    @Override
    public void createNewFeedback(String message,String patientId,String doctorId) {
        var  patientEntityOptional  = patientRepository.findPatientByPatientId(patientId).orElseThrow(() -> new ApiException("Patient not found"));
        var doctorEntityOptional = doctorRepository.findDoctorByDoctorId(doctorId).orElseThrow(() -> new ApiException("Doctor not found"));;

            var feedbackEntity = createFeedbackEntity(message, doctorEntityOptional, patientEntityOptional);
            feedbackRepository.save(feedbackEntity);


            patientEntityOptional.getFeedbacks().add(feedbackEntity);
            doctorEntityOptional.getFeedbacks().add(feedbackEntity);

            patientRepository.save(patientEntityOptional);
            doctorRepository.save(doctorEntityOptional);


    }
}

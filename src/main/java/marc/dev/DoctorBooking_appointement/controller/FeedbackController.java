package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.FeedbackRequest;
import marc.dev.DoctorBooking_appointement.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Collections.emptyMap;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = { "/api/feedbacks" })
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping()
//    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<Response> createFeedback(@AuthenticationPrincipal User userPrincipal,@RequestBody @Valid FeedbackRequest feedbackRequest,
                                                   @RequestParam("doctorId") String doctorId,

                                                   HttpServletRequest request) {
        System.out.println(userPrincipal);

        feedbackService.createNewFeedback(feedbackRequest.getMessage(), userPrincipal.getPatientId(), doctorId);
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Feedback submitted successfully", CREATED));
    }

    protected URI getUri() {
        return URI.create("");
    }
}

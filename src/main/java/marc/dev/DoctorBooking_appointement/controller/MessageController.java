package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dtorequest.MessageRequest;
import marc.dev.DoctorBooking_appointement.entity.MessageEntity;
import marc.dev.DoctorBooking_appointement.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = { "/api/messages" })
public class MessageController {
    private final MessageService messageService;


    @PostMapping()
    public ResponseEntity<Response> saveMessage(@RequestBody @Valid MessageRequest feedbackRequest, HttpServletRequest request) {
        messageService.createMessage(feedbackRequest.getEmail(), feedbackRequest.getSubject(), feedbackRequest.getMessage());
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Message has sent to the Medical staff.", CREATED));
    }

    @GetMapping()
    public ResponseEntity<Response> getMessages(HttpServletRequest request,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "8") int size) {
        Page<MessageEntity> feedbacks = messageService.getFeedbacks(page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("Messages", feedbacks), "feedback (s) retrieved", OK));
    }

    protected URI getUri() {
        return URI.create("");
    }
}

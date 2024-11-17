package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.AppointmentRequest;
import marc.dev.DoctorBooking_appointement.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = { "/users/patients/appointments" })
public class AppointmentController {
    private final AppointmentService appointmentService;



    @PostMapping
    public ResponseEntity<Response> bookAppointment(
            HttpServletRequest request,
            @AuthenticationPrincipal User userPrincipal,
            @RequestBody AppointmentRequest appointmentRequest,
            @RequestParam("localDateTime") LocalDateTime appointmentDateTime
    ){

        System.out.println(userPrincipal.getUserId());

        appointmentService.createAppointment(userPrincipal.getUserId(), appointmentRequest.getUserId(),appointmentDateTime);
        return ResponseEntity.created(
                        getUri())
                .body(getResponse(request, emptyMap(), "Appointment created Check your email", CREATED));


    }
    @GetMapping("/doctors")
    public ResponseEntity<Response> getDoctorAppointments(
            HttpServletRequest request,
            @AuthenticationPrincipal User userPrincipal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size

    ){


       var appointments= appointmentService.getDoctorAppointments(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", appointments), "Appointment(s) retrieved", OK));


    }

    @GetMapping
    public ResponseEntity<Response> getAppointments(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size

    ){


        var appointments= appointmentService.getAppointments(page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", appointments), "Appointment(s) retrieved", OK));


    }
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('USER', 'ADMIN','PATIENT','DOCTOR', 'STAFF','SUPER_ADMIN')")
    @GetMapping("/patients")
    public ResponseEntity<Response> getPatientAppointment(
            HttpServletRequest request,
            @AuthenticationPrincipal User userPrincipal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size

    ){


        var appointments= appointmentService.getPatientAppointments(userPrincipal.getUserId(), page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("patients", appointments), "Appointment(s) retrieved", OK));


    }

    protected URI getUri() {
        return URI.create("");
    }
}

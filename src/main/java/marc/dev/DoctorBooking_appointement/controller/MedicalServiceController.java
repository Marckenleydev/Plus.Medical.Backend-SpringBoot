package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dtorequest.ServiceRequest;
import marc.dev.DoctorBooking_appointement.dtorequest.UserRequest;
import marc.dev.DoctorBooking_appointement.entity.ServiceEntity;
import marc.dev.DoctorBooking_appointement.service.ServiceMedical;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = { "/api/medicalservices" })
public class MedicalServiceController {
    private final ServiceMedical serviceMedical;


    @PostMapping()
//    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('STAFF', 'ADMIN','PATIENT','DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Response> saveMedicalService(@RequestBody @Valid ServiceRequest serviceRequest, HttpServletRequest request) {
        serviceMedical.createMedicalService(serviceRequest.getTitle(), serviceRequest.getDescription());
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Medical Service created.", CREATED));
    }

    @GetMapping()
    public ResponseEntity<Response> getMedicalServices(HttpServletRequest request,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "8") int size) {
        Page<ServiceEntity> medicalServices = serviceMedical.getMedicalServices(page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("medicalServices", medicalServices), "Medical Service(s) retrieved", OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getMedicalService(@PathVariable("id") String id, HttpServletRequest request) {
        ServiceEntity medicalService = serviceMedical.getMedicalService(id);
        return ResponseEntity.ok().body(getResponse(request, Map.of("medicalService", medicalService), "Medical Service retrieved", OK));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('STAFF', 'ADMIN','PATIENT','DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updateMedicalService(@PathVariable("id") String id,
                                                         @RequestBody @Valid ServiceRequest serviceRequest,
                                                         HttpServletRequest request) {
        serviceMedical.updateMedicalService(id, serviceRequest.getTitle(), serviceRequest.getDescription());
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Medical Service updated", OK));
    }



    protected URI getUri() {
        return URI.create("");
    }
}

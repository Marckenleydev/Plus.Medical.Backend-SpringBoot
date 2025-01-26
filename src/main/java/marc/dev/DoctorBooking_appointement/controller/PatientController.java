package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.*;
import marc.dev.DoctorBooking_appointement.handle.ApiLogoutHandler;
import marc.dev.DoctorBooking_appointement.service.FeedbackService;
import marc.dev.DoctorBooking_appointement.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;


import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/users/patients" })
public class PatientController {
    private final PatientService patientService;
    private final FeedbackService feedbackService;
    private final ApiLogoutHandler apiLogoutHandler;


    @PostMapping("/register")
    public ResponseEntity<Response> savePatient(@RequestBody @Valid UserRequest user, HttpServletRequest request) {

        patientService.createPatient(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getImageUrl());


        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Check your email to enable your account.", CREATED));
    }


    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
        patientService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Account verified.", OK));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('USER', 'ADMIN','PATIENT','DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, HttpServletRequest request) {
        System.out.println("UserPrincipal: " + userPrincipal);
        var patient = patientService.getPatientByUserId(userPrincipal.getUserId());
        return ResponseEntity.ok().body(getResponse(request,  of("patient", patient), "Profile retrieve.", OK));
    }

    @GetMapping()
    public ResponseEntity<Response> getPatients(HttpServletRequest request,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "8") int size){
        var patients = patientService.getPatients(page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", patients), "Patients(s) retrieved", OK));
    }
    @GetMapping("/search")
    public ResponseEntity<Response> searchDoctors(HttpServletRequest request,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "8") int size,
                                                  @RequestParam(value = "name", defaultValue = "") String name){
        var patients = patientService.getPatients(page, size,name);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", patients), "Patients(s) retrieved", OK));
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, @RequestBody UserRequest userRequest, HttpServletRequest request) {
        var patient = patientService.updatePatient(userPrincipal.getUserId(), userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPhone(),userRequest.getMedical_history());
        return ResponseEntity.ok().body(getResponse(request,  of("user", patient), "Profile updated successfully", OK));
    }

    @PatchMapping("/updaterole")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updateRole(@AuthenticationPrincipal User userPrincipal, @RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        patientService.updateRole(userPrincipal.getUserId(), roleRequest.getRole());
        return ResponseEntity.ok().body(getResponse(request,   emptyMap(), "Account updated successfully", OK));
    }

    @PatchMapping("/update_password")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updatePassword(@AuthenticationPrincipal User userPrincipal, @RequestBody UpdatePasswordRequest passwordRequest, HttpServletRequest request) {
        patientService.updatePassword(userPrincipal.getUserId(),passwordRequest.getPassword(), passwordRequest.getNewPassword(), passwordRequest.getConfirmNewPassword() );
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Password updated successfully", OK));
    }



    @PostMapping("/send_feedbacks")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<Response> sendFeedback(@AuthenticationPrincipal User userPrincipal,@RequestBody @Valid FeedbackRequest feedbackRequest,
                                                   @RequestParam("doctorId") String doctorId,

                                                   HttpServletRequest request) {
        System.out.println(userPrincipal);

        feedbackService.createNewFeedback(feedbackRequest.getMessage(), userPrincipal.getPatientId(), doctorId);
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Feedback submitted successfully", CREATED));
    }
    // Start - reset password when user is NOT logged in
    @PostMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        patientService.resetPassword(emailRequest.getEmail());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Check your email to reset password.", OK));
    }

    @GetMapping("/verify/password")
    public ResponseEntity<Response> verifyPassword(@RequestParam("key") String key, HttpServletRequest request) {
        var patient = patientService.verifyPatientPasswordKey(key);

        return ResponseEntity.ok().body(getResponse(request, of("patient", patient), "Enter new Password. ", OK));
    }

    @PostMapping("/resetpassword/reset")
    public ResponseEntity<Response> doResetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        patientService.updatePassword(resetPasswordRequest.getUserId(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmNewPassword());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Password reset successfully ", OK));
    }
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        apiLogoutHandler.logout(request, response, authentication);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "You've logged out successfully ", OK));
    }
    // End - reset password when user is not logged in
    protected URI getUri() {
        return URI.create("");
    }
}

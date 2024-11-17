package marc.dev.DoctorBooking_appointement.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.*;
import marc.dev.DoctorBooking_appointement.handle.ApiLogoutHandler;
import marc.dev.DoctorBooking_appointement.service.DoctorService;
import marc.dev.DoctorBooking_appointement.service.JwtService;
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
@RequestMapping(path = { "/users/doctors" })
public class DoctorController {
    private final DoctorService doctorService;
    private final JwtService jwtService;
    private final ApiLogoutHandler apiLogoutHandler;

    @PostMapping("/register")
    public ResponseEntity<Response> saveDoctor(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        doctorService.createDoctor(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getSpecialisation());
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Account created. Check your email to enable your account.", CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
        doctorService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Account verified.", OK));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole( 'ADMIN','PATIENT','DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, HttpServletRequest request) {
        System.out.println("UserPrincipal: " + userPrincipal);
        var doctor = doctorService.getDoctorByUserId(userPrincipal.getUserId());
        return ResponseEntity.ok().body(getResponse(request,  of("doctor", doctor), "Profile retrieve.", OK));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<Response> profile(@PathVariable("doctorId") String doctorId, HttpServletRequest request) {

        var doctor = doctorService.getDoctorByDoctorId(doctorId);
        return ResponseEntity.ok().body(getResponse(request,  of("doctor", doctor), "Profile retrieve.", OK));
    }
    @GetMapping()
    public ResponseEntity<Response> getDoctors(HttpServletRequest request,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "8") int size){
        var doctors = doctorService.getDoctors(page, size);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", doctors), "Doctors(s) retrieved", OK));
    }
    @GetMapping("/search")
    public ResponseEntity<Response> searchDoctors(HttpServletRequest request,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "8") int size,
                                               @RequestParam(value = "name", defaultValue = "") String name){
        var doctors = doctorService.getDoctors(page, size,name);
        return ResponseEntity.ok().body(getResponse(request, Map.of("doctors", doctors), "Doctors(s) retrieved", OK));
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, @RequestBody UserRequest userRequest, HttpServletRequest request) {
        var doctor = doctorService.updateDoctor(userPrincipal.getUserId(), userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPhone());
        return ResponseEntity.ok().body(getResponse(request,  of("user", doctor), "Profile updated successfully", OK));
    }

    @PatchMapping("/updaterole")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updateRole(@AuthenticationPrincipal User userPrincipal, @RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        doctorService.updateRole(userPrincipal.getUserId(), roleRequest.getRole());
        return ResponseEntity.ok().body(getResponse(request,   emptyMap(), "Account updated successfully", OK));
    }
    @PatchMapping("/update_password")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updatePassword(@AuthenticationPrincipal User userPrincipal, @RequestBody UpdatePasswordRequest passwordRequest, HttpServletRequest request) {
        doctorService.updatePassword(userPrincipal.getUserId(),passwordRequest.getPassword(), passwordRequest.getNewPassword(), passwordRequest.getConfirmNewPassword() );
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Password updated successfully", OK));
    }

    // Start - reset password when user is NOT logged in
    @PostMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        doctorService.resetPassword(emailRequest.getEmail());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "We sent you an email to reset your Password. ", OK));
    }

    @GetMapping("/verify/password")
    public ResponseEntity<Response> verifyPassword(@RequestParam("key") String key, HttpServletRequest request) {
        var user = doctorService.verifyDoctorPasswordKey(key);

        return ResponseEntity.ok().body(getResponse(request, of("user", user), "Enter new Password. ", OK));
    }

    @PostMapping("/resetpassword/reset")
    public ResponseEntity<Response> doResetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        doctorService.updatePassword(resetPasswordRequest.getUserId(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmNewPassword());

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

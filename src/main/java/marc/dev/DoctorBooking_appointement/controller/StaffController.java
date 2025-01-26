package marc.dev.DoctorBooking_appointement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.*;
import marc.dev.DoctorBooking_appointement.handle.ApiLogoutHandler;

import marc.dev.DoctorBooking_appointement.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/users/staffs" })
public class StaffController {
    private final StaffService staffService;
    private final ApiLogoutHandler apiLogoutHandler;




    @PostMapping("/register")
    public ResponseEntity<Response> savePatient(@ModelAttribute @Valid UserRequest user, HttpServletRequest request) {
        System.out.println("Entering savePatient method");

        System.out.println(user.getFirstName());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        staffService.createStaff(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),user.getImageUrl());


        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Account created. Check your email to enable your account.", CREATED));
    }


    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);
        staffService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Account verified.", OK));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('USER', 'ADMIN','PATIENT','DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, HttpServletRequest request) {
        System.out.println("UserPrincipal: " + userPrincipal);
        var staff = staffService.getStaffByUserId(userPrincipal.getUserId());
        return ResponseEntity.ok().body(getResponse(request,  of("user", staff), "Profile retrieve.", OK));
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, @RequestBody UserRequest userRequest, HttpServletRequest request) {
        var staff = staffService.updateStaff(userPrincipal.getUserId(), userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPhone(),userRequest.getMedical_history());
        return ResponseEntity.ok().body(getResponse(request,  of("user", staff), "Profile updated successfully", OK));
    }

    @PatchMapping("/updaterole")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updateRole(@AuthenticationPrincipal User userPrincipal, @RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        staffService.updateRole(userPrincipal.getUserId(), roleRequest.getRole());
        return ResponseEntity.ok().body(getResponse(request,   emptyMap(), "Account updated successfully", OK));
    }

    @PatchMapping("/update_password")
    @PreAuthorize("hasAnyAuthority('user:update') or hasAnyRole( 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updatePassword(@AuthenticationPrincipal User userPrincipal, @RequestBody UpdatePasswordRequest passwordRequest, HttpServletRequest request) {
        staffService.updatePassword(userPrincipal.getUserId(),passwordRequest.getPassword(), passwordRequest.getNewPassword(), passwordRequest.getConfirmNewPassword() );
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Password updated successfully", OK));
    }


    // Start - reset password when user is NOT logged in
    @PostMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        staffService.resetPassword(emailRequest.getEmail());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "We sent you an email to reset your Password. ", OK));
    }

    @GetMapping("/verify/password")
    public ResponseEntity<Response> verifyPassword(@RequestParam("key") String key, HttpServletRequest request) {
        var staff = staffService.verifyStaffPasswordKey(key);

        return ResponseEntity.ok().body(getResponse(request, of("user", staff), "Enter new Password. ", OK));
    }

    @PostMapping("/resetpassword/reset")
    public ResponseEntity<Response> doResetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        staffService.updatePassword(resetPasswordRequest.getUserId(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmNewPassword());

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

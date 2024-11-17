package marc.dev.DoctorBooking_appointement.security;

import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.domain.ApiAuthentication;
import marc.dev.DoctorBooking_appointement.domain.UserPrincipal;
import marc.dev.DoctorBooking_appointement.service.DoctorService;
import marc.dev.DoctorBooking_appointement.service.PatientService;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.service.StaffService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

import static marc.dev.DoctorBooking_appointement.domain.ApiAuthentication.authenticated;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final StaffService staffService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);

        if ("DOCTOR".equalsIgnoreCase(apiAuthentication.getUserType())) {
            var doctor = doctorService.getDoctorByEmail(apiAuthentication.getEmail());
            if (doctor != null) {
                var doctorCredential = doctorService.getDoctorCredentialById(doctor.getId());

                var userDoctorPrincipal = new UserPrincipal(doctor, doctorCredential);
                validAccount.accept(userDoctorPrincipal);
                if (encoder.matches(apiAuthentication.getPassword(), doctorCredential.getPassword())) {
                    return authenticated(doctor, userDoctorPrincipal.getAuthorities());
                } else {
                    throw new BadCredentialsException("Email and/or password incorrect. Please try again");
                }
            }
        } else if ("PATIENT".equalsIgnoreCase(apiAuthentication.getUserType())) {
            var patient = patientService.getPatientByEmail(apiAuthentication.getEmail());
            if (patient != null) {
                var patientCredential = patientService.getPatientCredentialById(patient.getId());

                var userPatientPrincipal = new UserPrincipal(patient, patientCredential);
                validAccount.accept(userPatientPrincipal);
                if (encoder.matches(apiAuthentication.getPassword(), patientCredential.getPassword())) {
                    return authenticated(patient, userPatientPrincipal.getAuthorities());
                } else {
                    throw new BadCredentialsException("Email and/or password incorrect. Please try again");
                }
            }
        }
        else if ("STAFF".equalsIgnoreCase(apiAuthentication.getUserType())) {
            var staff = staffService.getStaffByEmail(apiAuthentication.getEmail());
            if (staff != null) {
                var staffCredential = staffService.getStaffCredentialById(staff.getId());

                var userStaffPrincipal = new UserPrincipal(staff, staffCredential);
                validAccount.accept(userStaffPrincipal);
                if (encoder.matches(apiAuthentication.getPassword(), staffCredential.getPassword())) {
                    return authenticated(staff, userStaffPrincipal.getAuthorities());
                } else {
                    throw new BadCredentialsException("Email and/or password incorrect. Please try again");
                }
            }
        }

        throw new ApiException("Unable to authenticate");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if (!userPrincipal.isAccountNonLocked()) {
            throw new LockedException("Your account is currently locked");
        }
        if (!userPrincipal.isEnabled()) {
            throw new DisabledException("Your account is currently disabled");
        }
        if (!userPrincipal.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Your password has expired. Please update your password");
        }
        if (!userPrincipal.isAccountNonExpired()) {
            throw new DisabledException("Your account has expired. Please contact administrator");
        }
    };
}

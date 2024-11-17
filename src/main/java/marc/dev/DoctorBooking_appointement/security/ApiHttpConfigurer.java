package marc.dev.DoctorBooking_appointement.security;

import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.service.DoctorService;
import marc.dev.DoctorBooking_appointement.service.JwtService;

import marc.dev.DoctorBooking_appointement.service.PatientService;
import marc.dev.DoctorBooking_appointement.service.StaffService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiHttpConfigurer extends AbstractHttpConfigurer<ApiHttpConfigurer, HttpSecurity> {
    private final AuthorizationFilter authorizationFilter;
    private final ApiAuthenticationProvider apiAuthenticationProvider;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final StaffService staffService;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.authenticationProvider(apiAuthenticationProvider);

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new ApiAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), patientService,doctorService,staffService, jwtService), UsernamePasswordAuthenticationFilter.class);
    }
}

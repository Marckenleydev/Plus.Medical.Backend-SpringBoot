package marc.dev.DoctorBooking_appointement.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.domain.Response;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.dtorequest.LoginRequest;
import marc.dev.DoctorBooking_appointement.enumeration.LoginType;

import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.service.DoctorService;
import marc.dev.DoctorBooking_appointement.service.JwtService;
import marc.dev.DoctorBooking_appointement.service.PatientService;
import marc.dev.DoctorBooking_appointement.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static java.util.Map.of;
import static marc.dev.DoctorBooking_appointement.constant.Constants.LOGIN_PATH;
import static marc.dev.DoctorBooking_appointement.domain.ApiAuthentication.unauthenticated;
import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.ACCESS;
import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.REFRESH;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.getResponse;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final StaffService staffService;
    private final JwtService jwtService;

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager, PatientService patientService, DoctorService doctorService,StaffService staffService, JwtService jwtService) {
        super(new AntPathRequestMatcher(LOGIN_PATH, POST.name()), authenticationManager);
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.staffService = staffService;
        this.jwtService = jwtService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            var loginRequest = new ObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), LoginRequest.class);

            if ("PATIENT".equalsIgnoreCase(loginRequest.getUserType())) {
                patientService.updateLoginAttempt(loginRequest.getEmail(), LoginType.LOGIN_ATTEMPT);
            } else if ("DOCTOR".equalsIgnoreCase(loginRequest.getUserType())) {
                doctorService.updateLoginAttempt(loginRequest.getEmail(), LoginType.LOGIN_ATTEMPT);
            }else if ("STAFF".equalsIgnoreCase(loginRequest.getUserType())) {
                staffService.updateLoginAttempt(loginRequest.getEmail(), LoginType.LOGIN_ATTEMPT);
            }  else {
                throw new ApiException("Unknown user type: " + loginRequest.getUserType());
            }

            var authentication = unauthenticated(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getUserType());
            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception exception) {
            log.error("Error during attemptAuthentication: {}", exception.getMessage(), exception);
            handleErrorResponse(request, response, exception);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var user = (User) authentication.getPrincipal();
        if ("PATIENT".equalsIgnoreCase(user.getRole())) {
            patientService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        } else if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            doctorService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        }else if ("STAFF".equalsIgnoreCase(user.getRole())) {
            staffService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        }

        var httpResponse = sendResponse(request, response, user);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());

        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user,REFRESH);
        return getResponse(request, of("user", user), "Login Success", HttpStatus.OK);
    }
}

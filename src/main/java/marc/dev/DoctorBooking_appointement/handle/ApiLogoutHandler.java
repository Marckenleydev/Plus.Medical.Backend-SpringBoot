package marc.dev.DoctorBooking_appointement.handle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import marc.dev.DoctorBooking_appointement.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.ACCESS;
import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.REFRESH;

@Service
@RequiredArgsConstructor
public class ApiLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        jwtService.removeCookie(request,response, ACCESS.getValue());
        jwtService.removeCookie(request,response, REFRESH.getValue());
    }
}



package marc.dev.DoctorBooking_appointement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.domain.Token;
import marc.dev.DoctorBooking_appointement.domain.TokenData;
import marc.dev.DoctorBooking_appointement.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static java.util.Arrays.asList;
import static marc.dev.DoctorBooking_appointement.constant.Constants.PUBLIC_ROUTES;
import static marc.dev.DoctorBooking_appointement.domain.ApiAuthentication.authenticated;
import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.ACCESS;
import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.REFRESH;
import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.OPTIONS;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessToken = jwtService.extractToken(request, ACCESS.getValue());
            if (accessToken.isPresent()) {
                String token = accessToken.get();
                if (jwtService.isDoctorToken(token)) {
                    handleDoctorToken(token, request, response);
                } else if (jwtService.isStaffToken(token)) {
                    handleStaffToken(token, request, response);
                } else {
                    handlePatientToken(token, request, response);
                }
            } else {
                Optional<String> refreshToken = jwtService.extractToken(request, REFRESH.getValue());
                if (refreshToken.isPresent()) {
                    String token = refreshToken.get();
                    if (jwtService.isDoctorToken(token)) {
                        handleDoctorRefreshToken(token, request, response);
                    } else if (jwtService.isStaffToken(token)) {
                        handleStaffRefreshToken(token, request, response);
                    } else {
                        handlePatientRefreshToken(token, request, response);
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
        }
    }

    private void handleDoctorToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getDoctorTokenData(token, TokenData::isValid)) {
            SecurityContextHolder.getContext().setAuthentication(getDoctorAuthentication(token, request));
            RequestContext.setUserId(jwtService.getDoctorTokenData(token, TokenData::getUser).getId());
        }
    }

    private void handlePatientToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getTokenData(token, TokenData::isValid)) {
            SecurityContextHolder.getContext().setAuthentication(getPatientAuthentication(token, request));
            RequestContext.setUserId(jwtService.getTokenData(token, TokenData::getUser).getId());
        }
    }

    private void handleStaffToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getStaffTokenData(token, TokenData::isValid)) {
            SecurityContextHolder.getContext().setAuthentication(getStaffAuthentication(token, request));
            RequestContext.setUserId(jwtService.getStaffTokenData(token, TokenData::getUser).getId());
        }
    }

    private void handleDoctorRefreshToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getDoctorTokenData(token, TokenData::isValid)) {
            var user = jwtService.getDoctorTokenData(token, TokenData::getUser);
            SecurityContextHolder.getContext().setAuthentication(getDoctorAuthentication(jwtService.createToken(user, Token::getAccess), request));
            jwtService.addCookie(response, user, ACCESS);
            RequestContext.setUserId(user.getId());
        }
    }

    private void handlePatientRefreshToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getTokenData(token, TokenData::isValid)) {
            var user = jwtService.getTokenData(token, TokenData::getUser);
            SecurityContextHolder.getContext().setAuthentication(getPatientAuthentication(jwtService.createToken(user, Token::getAccess), request));
            jwtService.addCookie(response, user, ACCESS);
            RequestContext.setUserId(user.getId());
        }
    }

    private void handleStaffRefreshToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.getStaffTokenData(token, TokenData::isValid)) {
            var user = jwtService.getStaffTokenData(token, TokenData::getUser);
            SecurityContextHolder.getContext().setAuthentication(getStaffAuthentication(jwtService.createToken(user, Token::getAccess), request));
            jwtService.addCookie(response, user, ACCESS);
            RequestContext.setUserId(user.getId());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean shouldNotFilter = request.getMethod().equalsIgnoreCase(OPTIONS.name()) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
        if (shouldNotFilter) {
            RequestContext.setUserId(0L);
        }
        return shouldNotFilter;
    }

    private Authentication getDoctorAuthentication(String token, HttpServletRequest request) {
        var authentication = authenticated(jwtService.getDoctorTokenData(token, TokenData::getUser), jwtService.getDoctorTokenData(token, TokenData::getAuthorities));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    private Authentication getPatientAuthentication(String token, HttpServletRequest request) {
        var authentication = authenticated(jwtService.getTokenData(token, TokenData::getUser), jwtService.getTokenData(token, TokenData::getAuthorities));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    private Authentication getStaffAuthentication(String token, HttpServletRequest request) {
        var authentication = authenticated(jwtService.getStaffTokenData(token, TokenData::getUser), jwtService.getStaffTokenData(token, TokenData::getAuthorities));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}

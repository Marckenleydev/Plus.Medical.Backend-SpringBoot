//package marc.dev.DoctorBooking_appointement.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import marc.dev.DoctorBooking_appointement.domain.RequestContext;
//import marc.dev.DoctorBooking_appointement.domain.Token;
//import marc.dev.DoctorBooking_appointement.domain.TokenData;
//import marc.dev.DoctorBooking_appointement.service.JwtService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//import static java.util.Arrays.asList;
//import static marc.dev.DoctorBooking_appointement.constant.Constants.PUBLIC_ROUTES;
//import static marc.dev.DoctorBooking_appointement.domain.ApiAuthentication.authenticated;
//import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.ACCESS;
//import static marc.dev.DoctorBooking_appointement.enumeration.TokenType.REFRESH;
//import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.handleErrorResponse;
//import static org.springframework.http.HttpMethod.OPTIONS;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AuthorizationDoctorFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            var accessToken = jwtService.extractToken(request, ACCESS.getValue());
//            if(accessToken.isPresent() && jwtService.getDoctorTokenData(accessToken.get(), TokenData::isValid)){
//
//                SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken.get(), request));
//                RequestContext.setUserId(jwtService.getDoctorTokenData(accessToken.get(), TokenData::getUser).getId());
//            }else {
//                var refreshToken = jwtService.extractToken(request, REFRESH.getValue());
//                if(refreshToken.isPresent() && jwtService.getDoctorTokenData(refreshToken.get(), TokenData::isValid)){
//                    var user = jwtService.getDoctorTokenData(refreshToken.get(), TokenData::getUser);
//                    SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtService.createToken(user, Token::getAccess),request));
//                    jwtService.addCookie(response, user, ACCESS);
//                    RequestContext.setUserId(user.getId());
////                    log.info(String.valueOf(user.getId()));
//                }else {
//                    SecurityContextHolder.clearContext();
//                }
//            }
//            filterChain.doFilter(request,response);
//
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            handleErrorResponse(request, response, exception);
//        }
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        //var shouldNotFilter = request.getMethod().equalsIgnoreCase(OPTIONS.name()) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
//        var shouldNotFilter = request.getMethod().equalsIgnoreCase(OPTIONS.name()) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
//        if(shouldNotFilter) { RequestContext.setUserId(0L); }
//        return shouldNotFilter;
//    }
//
//    private Authentication getAuthentication(String token, HttpServletRequest request) {
//        var authentication = authenticated(jwtService.getDoctorTokenData(token, TokenData::getUser), jwtService.getDoctorTokenData(token, TokenData::getAuthorities));
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        return authentication;
//    }
//}

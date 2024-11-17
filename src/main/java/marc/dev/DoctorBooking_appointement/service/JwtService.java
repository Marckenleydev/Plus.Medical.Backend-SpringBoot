package marc.dev.DoctorBooking_appointement.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marc.dev.DoctorBooking_appointement.domain.Token;
import marc.dev.DoctorBooking_appointement.domain.TokenData;

import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.enumeration.TokenType;

import java.util.Optional;
import java.util.function.Function;

public interface JwtService {
    boolean isDoctorToken(String token);

     boolean isStaffToken(String token);
    String createToken(User user, Function<Token, String> tokenFunction);

    Optional<String> extractToken(HttpServletRequest request, String tokenType);
    void addCookie(HttpServletResponse response, User user, TokenType type);

    <T> T getTokenData(String token, Function<TokenData, T> tokenFunction);
    <T> T getDoctorTokenData(String token, Function<TokenData, T> tokenFunction);
    <T> T getStaffTokenData(String token, Function<TokenData, T> tokenFunction);
    void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);
}

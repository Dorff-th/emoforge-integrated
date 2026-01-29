package dev.emoforge.auth.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LoginResponseHandler {

    @Value("${security.cookie.secure}")
    private boolean secure;

    @Value("${security.cookie.access-domain}")
    private String accessDomain;

    @Value("${security.cookie.same-site}")
    private String sameSite;

    public void setLoginCookies(
            HttpServletResponse response,
            String accessToken,
            String refreshToken
    ) {

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(accessDomain)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(accessDomain)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}

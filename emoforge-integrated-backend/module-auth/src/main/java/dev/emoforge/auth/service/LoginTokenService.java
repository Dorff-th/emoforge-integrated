package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;

import dev.emoforge.auth.token.JwtTokenIssuer;
import dev.emoforge.core.properties.CookieProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class LoginTokenService {

    private final JwtTokenIssuer jwtTokenIssuer;
    private final CookieProvider cookieProvider;

    public void handleLoginSuccess(
            HttpServletResponse response,
            Member member,
            LoginType loginType
    ) {
        String accessToken = jwtTokenIssuer.createAccessToken(
                member.getRole().name(),
                member.getUuid()
        );

        String refreshToken = jwtTokenIssuer.createRefreshToken(
                member.getUuid()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createAccessTokenCookie(accessToken).toString()
        );
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(refreshToken).toString()
        );

        log.info("Login success: uuid={}, role={}, type={}",
                member.getUuid(),
                member.getRole(),
                loginType
        );
    }

    public void handleLogout(HttpServletResponse response, LoginType loginType) {

        if (loginType == LoginType.ADMIN) {
            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    cookieProvider.deleteAdminTokenCookie().toString()
            );
            return;
        }

        cookieProvider.deleteAllUserCookies()
                .forEach(cookie ->
                        response.addHeader(
                                HttpHeaders.SET_COOKIE,
                                cookie.toString()
                        )
                );
    }

    public void handleTokenRefresh(
            HttpServletResponse response,
            Member member
    ) {
        String newAccessToken = jwtTokenIssuer.createAccessToken(
                member.getRole().name(),
                member.getUuid()
        );

        String newRefreshToken = jwtTokenIssuer.createRefreshToken(
                member.getUuid()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createAccessTokenCookie(newAccessToken).toString()
        );
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieProvider.createRefreshTokenCookie(newRefreshToken).toString()
        );
    }

}


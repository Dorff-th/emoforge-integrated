package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;

import dev.emoforge.auth.repository.RefreshTokenRepository;
import dev.emoforge.auth.token.JwtTokenIssuer;
import dev.emoforge.core.properties.CookieProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
public class LoginTokenService {

    private final JwtTokenIssuer jwtTokenIssuer;
    private final CookieProvider cookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    //0317 추가
    private final RefreshTokenService refreshTokenService;

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }

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

        //0317 - DB에서 기존 refresh_token을 먼저 삭제하고, 신규 발급된 refresh_token을 save(insert)한다.
        refreshTokenService.rotateRefreshToken(member.getUuid(), refreshToken);

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

    //0317 수정 : 로그아웃 되면 DB에 저장된 refresh_token 삭제
    @Transactional
    public void handleLogout(String refreshToken, HttpServletResponse response) {

        String tokenHash = hash(refreshToken);

        refreshTokenRepository.deleteByTokenHash(tokenHash);
     

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

        //0317 - DB에서 기존 refresh_token을 먼저 삭제하고, 신규 발급된 refresh_token을 save(insert)한다.
        refreshTokenService.rotateRefreshToken(member.getUuid(), newRefreshToken);

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


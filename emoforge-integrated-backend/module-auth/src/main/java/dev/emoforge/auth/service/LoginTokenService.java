package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;
import dev.emoforge.auth.response.LoginResponseHandler;
import dev.emoforge.auth.token.JwtTokenIssuer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class LoginTokenService {

    private final JwtTokenIssuer jwtTokenIssuer;
    private final LoginResponseHandler loginResponseHandler;

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

        loginResponseHandler.setLoginCookies(
                response,
                accessToken,
                refreshToken
        );

        // 필요하면 audit/log
        log.info("Login success: uuid={}, role={}, type={}",
                member.getUuid(),
                member.getRole(),
                loginType
        );
    }
}

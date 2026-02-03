package dev.emoforge.app.common.properties;

import dev.emoforge.core.properties.CookieProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultCookieProvider implements CookieProvider {

    private final CookieProperties props;

    public DefaultCookieProvider(CookieProperties props) {
        this.props = props;
    }

    @Override
    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(props.getAccess().getName(), token)
                .domain(props.getCommon().getDomain())
                .secure(props.getCommon().isSecure())
                .httpOnly(props.getAccess().isHttpOnly())
                .sameSite(props.getCommon().getSameSite())
                .path("/")
                .maxAge(Duration.ofHours(props.getAccess().getExpirationHours()))
                .build();
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(props.getRefresh().getName(), token)
                .domain(props.getCommon().getDomain())
                .secure(props.getCommon().isSecure())
                .httpOnly(props.getRefresh().isHttpOnly())
                .sameSite(props.getCommon().getSameSite())
                .path("/")
                .maxAge(Duration.ofDays(props.getRefresh().getExpirationDays()))
                .build();
    }



    @Override
    public List<ResponseCookie> deleteAllUserCookies() {

        List<ResponseCookie> cookies = new ArrayList<>();

        // 1️⃣ 정상 domain 기준
        cookies.add(deleteAccessTokenCookie());
        cookies.add(deleteRefreshTokenCookie());

        // 2️⃣ domain 없는 변형 (브라우저별 잔여 대응)
        cookies.add(ResponseCookie.from(props.getAccess().getName(), "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite(props.getCommon().getSameSite())
                .maxAge(0)
                .build()
        );

        cookies.add(ResponseCookie.from(props.getRefresh().getName(), "")
                .path("/")
                .httpOnly(true)
                .secure(props.getCommon().isSecure())
                .sameSite(props.getCommon().getSameSite())
                .maxAge(0)
                .build()
        );

        // 3️⃣ JSESSIONID 정리 (OAuth2Login 잔여 대비)
        cookies.add(ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .maxAge(0)
                .build()
        );

        return cookies;
    }

    @Override
    public ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(props.getAccess().getName(), "")
                .domain(props.getCommon().getDomain())
                .path("/")
                .secure(props.getCommon().isSecure())
                .httpOnly(props.getAccess().isHttpOnly())
                .sameSite(props.getCommon().getSameSite())
                .maxAge(0)
                .build();
    }

    @Override
    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(props.getRefresh().getName(), "")
                .domain(props.getCommon().getDomain())
                .path("/")
                .secure(props.getCommon().isSecure())
                .httpOnly(props.getRefresh().isHttpOnly())
                .sameSite(props.getCommon().getSameSite())
                .maxAge(0)
                .build();
    }




}

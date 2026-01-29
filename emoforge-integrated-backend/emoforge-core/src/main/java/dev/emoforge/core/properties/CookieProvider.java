package dev.emoforge.core.properties;

import org.springframework.http.ResponseCookie;

import java.util.List;

// emoforge-core
public interface CookieProvider {

    ResponseCookie createAccessTokenCookie(String token);

    ResponseCookie createRefreshTokenCookie(String token);

    // ✅ 여기 추가
    ResponseCookie deleteAccessTokenCookie();
    ResponseCookie deleteRefreshTokenCookie();

    @Deprecated
    ResponseCookie createAdminTokenCookie(String token);

    @Deprecated
    ResponseCookie deleteAdminTokenCookie();

    // ✅ 추가: 변형까지 포함한 삭제 세트
    List<ResponseCookie> deleteAllUserCookies();
}

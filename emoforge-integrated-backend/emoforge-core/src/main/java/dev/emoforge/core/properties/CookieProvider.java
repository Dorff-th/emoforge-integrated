package dev.emoforge.core.properties;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.time.LocalDateTime;
import java.util.List;

// emoforge-core
public interface CookieProvider {

    ResponseCookie createAccessTokenCookie(String token);

    ResponseCookie createRefreshTokenCookie(String token);

    // ✅ 여기 추가
    ResponseCookie deleteAccessTokenCookie();
    ResponseCookie deleteRefreshTokenCookie();

    // ✅ 추가: 변형까지 포함한 삭제 세트
    List<ResponseCookie> deleteAllUserCookies();

    //0317 : 쿠키에서 refresh_token 추출하는 메서드
    String extractRefreshTokenFromCookie(HttpServletRequest request);
}

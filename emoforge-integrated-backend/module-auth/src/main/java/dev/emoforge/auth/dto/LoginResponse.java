package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일반 로그인 응답 DTO.
 *
 * ⚠️ 현재 프로젝트에서는 사용되지 않음.
 * - 일반 사용자(ROLE_USER)는 Kakao OAuth2 로그인 방식을 사용하며
 *   JWT 토큰은 쿠키(access_token, refresh_token)로 관리됨.
 * - 관리자 로그인은 AdminLoginResponse를 통해 처리됨.
 *
 * 이 DTO는 "username/password 방식의 로그인"을 도입할 경우 대비한 구조로,
 * AuthController.login() (POST /api/auth/login) 에서 사용될 수 있다.
 */
@Schema(
        description = """
                일반 로그인 응답 DTO.

                ⚠ 현재는 사용되지 않음.
                - 사용자 로그인은 Kakao OAuth2 방식 사용
                - JWT는 HttpOnly 쿠키 형태로 발급되므로 이 DTO가 반환되지 않음
                - 관리자 로그인은 AdminLoginResponse 사용

                향후 username/password 로그인 방식을 다시 확장할 경우 사용될 수 있다.

                사용 예상 API (현재 비활성):
                - POST /api/auth/login
                """
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    @Schema(
            description = "Access Token (현재 OAuth2에서는 사용되지 않음)",
            example = "eyJhbGciOiJIUzI1NiIsInR..."
    )
    private String accessToken;

    @Schema(
            description = "Refresh Token (현재 OAuth2에서는 사용되지 않음)",
            example = "eyJhbGciOiJIUzUxMiIsInR..."
    )
    private String refreshToken;

    @Schema(
            description = "토큰 타입 (default: Bearer)",
            example = "Bearer",
            defaultValue = "Bearer"
    )
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(
            description = "Access Token의 만료 시간(초 단위)",
            example = "3600",
            defaultValue = "3600"
    )
    @Builder.Default
    private Long expiresIn = 3600L; // 1시간 (초 단위)
}

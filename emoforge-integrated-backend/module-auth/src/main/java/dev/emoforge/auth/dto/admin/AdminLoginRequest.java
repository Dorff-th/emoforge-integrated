package dev.emoforge.auth.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 관리자 로그인 요청 DTO
 * - 관리자 계정(username, password) 기반으로 로그인 요청을 수행한다.
 * - 요청된 password는 서버 내부에서 PasswordEncoder(BCrypt 등)로 검증된다.
 * - admin_token(JWT)은 HttpOnly 쿠키로 발급되므로 프런트는 직접 토큰을 읽지 않는다.
 * - captchaToken은 reCAPTCHA 또는 자체 캡챠 토큰 전달 목적이며 선택적이다.
 *
 * 🔎 사용되는 컨트롤러 / 메서드:
 * - AdminAuthController.login(@RequestBody AdminLoginRequest request)
 *   → /api/auth/admin/login (POST)
 *
 *   사용 API 목록:
 * - POST /api/auth/admin/login
 *
 * 즉, 관리자 인증 플로우의 진입점에서만 사용되는 전용 DTO이다.
 */
@Schema(
        description = """
                관리자 로그인 요청 DTO (Admin Only).
                이 DTO는 관리자 인증 흐름의 진입점인 AdminAuthController.login()에서 사용된다.
                비밀번호는 PasswordEncoder(BCrypt 등)로 서버에서 검증되며 평문 저장되지 않는다.
                captchaToken은 reCAPTCHA 또는 자체 캡챠 검증에 사용된다. \n
                사용 API 목록: POST /api/auth/admin/login
                """
)
public record AdminLoginRequest(
        @Schema(
                description = "관리자 계정명",
                example = "admin",
                required = true
        )
        @NotBlank String username,

        @Schema(
                description = """
                        관리자 비밀번호 (평문으로 전달되지만 서버 내부에서는 PasswordEncoder로 검증됨).
                        저장 시 평문을 보관하지 않으며, BCrypt 등 해시 알고리즘을 사용하여 안전하게 처리한다.
                        """,
                example = "superSecurePassword123!",
                required = true
        )
        @NotBlank String password,

        @Schema(
                description = """
                        reCAPTCHA 검증용 토큰 또는 자체 캡챠 사용 시 전달되는 값.
                        (캡챠를 사용하지 않는 환경에서는 null 가능)
                        """,
                example = "03AFcWeA...RecaptchaTokenValue...",
                required = false
        )
        String captchaToken
) {}

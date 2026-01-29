package dev.emoforge.auth.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 관리자 로그인 응답 DTO
 *
 * - 관리자 로그인 성공 시 반환되는 응답 구조.
 * - JWT(admin_token)는 HttpOnly 쿠키로 전달되므로, 프런트는 직접 토큰을 읽지 않는다.
 * - 따라서 프런트 화면 표시와 인증 상태 관리 편의를 위해 message 및 expiresInSeconds 값을 함께 내려준다.
 *
 * 🔎 사용되는 API:
 * - AdminAuthController.login()
 *   → POST /api/auth/admin/login
 */
@Schema(
        description = """
                관리자 로그인 응답 DTO.
                
                사용 API:
                - AdminAuthController.login()
                  → POST /api/auth/admin/login

                admin_token(JWT)은 HttpOnly 쿠키로 전달되며,
                프런트는 이 DTO의 message·expiresInSeconds를 사용해
                인증 성공 여부 표시 및 만료 타이머를 구현할 수 있다.
                """
)
public record AdminLoginResponse(

        @Schema(
                description = "로그인 처리 결과 메시지",
                example = "관리자 로그인 성공"
        )
        String message

) {}

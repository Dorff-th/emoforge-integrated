package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 일반 로그인 요청 DTO.
 *
 * ⚠️ 현재 프로젝트에서는 실제로 사용되지 않음.
 * - 일반 사용자(ROLE_USER)는 OAuth2(Kakao) 로그인 방식 사용
 * - 관리자 로그인은 AdminLoginRequest(AdminAuthController) 사용
 *
 * 따라서 이 DTO는 "향후 확장 가능성을 위해 유지 중인 로그인 요청 정보"에 해당한다.
 *
 * 🔎 사용 예상 API (현재는 비활성 상태):
 * - AuthController.login()
 *   → POST /api/auth/login
 *
 * username/password 기반 인증이 필요한 상황(테스트용 계정, 내부용 로그인 등)을 위해 문서화만 수행한다.
 */
@Schema(
        description = """
                일반 로그인 요청 DTO.

                ⚠ 현재는 사용되지 않음:
                - 사용자 로그인은 Kakao OAuth2 사용
                - 관리자 로그인은 '/api/auth/admin/login' 사용

                이 DTO는 username/password 기반의 로그인 방식이 필요해질 때 사용될 예정이다.

                사용 API (잠재적):
                - POST /api/auth/login
                """
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(
            description = "사용자 계정 ID (OAuth2가 아닌 순수 ID/PW 방식에서 사용됨)",
            example = "testuser"
    )
    @NotBlank(message = "사용자명은 필수입니다")
    private String username;

    @Schema(
            description = "사용자의 비밀번호 (PasswordEncoder로 검증됨)",
            example = "p@ssw0rd!"
    )
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}

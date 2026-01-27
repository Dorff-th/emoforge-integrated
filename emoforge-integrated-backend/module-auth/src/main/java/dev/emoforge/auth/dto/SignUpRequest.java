package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 일반 회원가입 요청 DTO.
 *
 * ⚠️ 현재 프로젝트에서는 사용되지 않음.
 *
 * 👇 그 이유:
 * - 일반 사용자(ROLE_USER)는 "카카오 OAuth2 로그인"만 제공함
 * - 사용자가 카카오 로그인에 최초 진입하면
 *   ▶ Auth-Service에서 Kakao 사용자 ID가 member 테이블에 없는 경우
 *      자동으로 회원가입 처리됨 (이 DTO를 사용하지 않음)
 *
 * 따라서 이 클래스는 "ID/PW 기반 회원가입 기능을 다시 도입할 경우"를 대비해 유지되는 구조이다.
 *
 * 🔎 사용될 수 있는 API (현재 비활성):
 * - AuthController.signUp()
 *   → POST /api/auth/signup
 */
@Schema(
        description = """
                일반 회원가입 요청 DTO.

                ⚠ 현재는 사용되지 않음.
                - 로그인 및 회원가입은 Kakao OAuth2 기반으로 처리됨
                - 신규 카카오 로그인 사용자는 컨트롤러를 거치지 않고 자동 가입됨

                향후 ID/PW 기반 회원가입 기능을 확장할 때 사용될 수 있다.

                사용 예상 API (비활성):
                - POST /api/auth/signup
                """
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Schema(
            description = "사용자 계정 ID (OAuth2 기반 회원가입에서는 사용되지 않음)",
            example = "new_user123"
    )
    @NotBlank(message = "사용자명은 필수입니다")
    private String username;

    @Schema(
            description = "사용자 비밀번호 (PasswordEncoder로 암호화될 값)",
            example = "p@ssw0rd!"
    )
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @Schema(
            description = "사용자 닉네임",
            example = "developer_kim"
    )
    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    @Schema(
            description = "사용자 이메일 주소",
            example = "user@example.com"
    )
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
}

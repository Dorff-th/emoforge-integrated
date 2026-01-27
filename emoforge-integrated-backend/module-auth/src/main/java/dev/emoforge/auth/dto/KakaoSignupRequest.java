package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * KakaoSignupRequest
 *
 * 카카오 신규 회원가입 시 필요한 핵심 정보를 담는 요청 DTO.
 *
 * ✔ 사용 시점
 * - 카카오 로그인 1단계 결과가 `NEED_AGREEMENT` 인 경우
 * - 프론트엔드에서 이용약관 동의 후 이 요청을 보내 회원가입이 진행됨
 *
 * ✔ 필드 설명
 * - kakaoId : 카카오 계정의 고유 ID (숫자값)
 * - nickname : 카카오 프로필 닉네임 (회원 최초 생성 시 저장)
 * - email : 초기에는 랜덤 생성되지만 이후 사용자 설정 페이지에서 변경 가능
 *
 * 회원가입 성공 시 Auth-Service는 AccessToken / RefreshToken 쿠키를 발급하며,
 * 신규 회원은 자동으로 로그인된 상태가 된다.
 */
@Schema(description = "카카오 신규 회원가입 요청 DTO")
@Getter
@Setter
public class KakaoSignupRequest {

    @Schema(
            description = "카카오 계정의 고유한 ID",
            example = "2877349238"
    )
    @NotBlank
    private Long kakaoId;

    @Schema(
            description = "카카오 프로필 닉네임",
            example = "행복한 코딩러"
    )
    @NotBlank
    private String nickname;

    // email은 초기에는 랜덤 생성이지만, 후에 사용자 변경 가능
    @Schema(
            description = """
                    신규 회원의 이메일 주소.
                    초기에는 랜덤 생성되지만 이후 프로필 설정 화면에서 변경 가능.
                    """,
            example = "tempuser_298371@example.com"
    )
    @NotBlank
    private String email;
}

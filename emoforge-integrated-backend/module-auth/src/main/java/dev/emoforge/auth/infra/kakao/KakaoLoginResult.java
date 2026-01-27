package dev.emoforge.auth.infra.kakao;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KakaoLoginResult
 *
 * 카카오 OAuth 로그인 처리 후 Auth-Service가 프론트엔드에 반환하는 결과 DTO.
 *
 * ✔ 주요 용도
 * - 기존 회원 여부에 따라 로그인 성공 또는 신규가입 플로우를 프론트엔드가 판단할 수 있도록 정보 제공
 *
 * ✔ status 값
 *   • LOGIN_OK        : 기존 회원이며 정상 로그인 처리 완료 (JWT 쿠키 생성됨)
 *   • NEED_AGREEMENT : 신규 회원으로 판단, 이용약관 동의 필요 (JWT 쿠키 없음)
 *
 * ✔ 필드 설명
 *   - status  : 로그인 결과 상태 (LOGIN_OK / NEED_AGREEMENT)
 *   - kakaoId : 신규가입 시 필요한 고유 카카오 ID (기존 회원이면 null)
 *   - nickname: 카카오 프로필 닉네임 (신규가입 UX를 위해 제공)
 *
 * 프론트엔드는 status 값을 기반으로
 *   • LOGIN_OK        → 사용자 홈/프로필 페이지 이동
 *   • NEED_AGREEMENT → 이용약관 화면으로 이동
 * 과 같이 분기 처리한다.
 */
@Schema(description = "카카오 로그인 처리 결과 DTO")
public record KakaoLoginResult(

        @Schema(
                description = "로그인 처리 상태",
                example = "LOGIN_OK",
                allowableValues = { "LOGIN_OK", "NEED_AGREEMENT" }
        )
        String status,      // LOGIN_OK / NEED_AGREEMENT

        @Schema(
                description = "카카오 사용자 ID (신규가입 시 필요, 기존 회원은 null)",
                example = "2877349238"
        )
        String kakaoId,     // 신규회원 시 필요

        @Schema(
                description = "카카오 프로필 닉네임",
                example = "행복한 코딩러"
        )
        String nickname
) {}

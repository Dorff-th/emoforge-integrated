package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * KakaoSignupResponse
 *
 * 카카오 신규 회원가입이 성공적으로 완료된 후 반환되는 응답 DTO.
 *
 * ✔ 주요 역할
 * - 회원가입 완료 상태 코드 전달 (status = "SIGNED_UP")
 * - 신규 생성된 회원의 UUID 반환
 * - 초기 저장된 nickname 반환
 *
 * 이 응답은 프론트엔드에서 회원가입 완료 후 자동 로그인된 상태로
 * 사용자 홈 또는 프로필 화면 등으로 라우팅할 때 사용된다.
 */
@Schema(description = "카카오 신규 회원가입 성공 응답 DTO")
@Builder
public record KakaoSignupResponse(

        @Schema(
                description = "회원가입 처리 상태 (항상 SIGNED_UP)",
                example = "SIGNED_UP"
        )
        String status,

        @Schema(
                description = "신규 생성된 회원의 UUID",
                example = "32b8b3a6-134f-48a5-a3bb-c6e33f988148"
        )
        String uuid,

        @Schema(
                description = "카카오 프로필에서 가져온 초깃값 닉네임",
                example = "행복한 코딩러"
        )
        String nickname
) {}

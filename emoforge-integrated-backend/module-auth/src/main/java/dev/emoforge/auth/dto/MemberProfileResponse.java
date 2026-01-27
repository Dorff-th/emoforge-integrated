package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 회원 프로필 응답 DTO.
 * - 회원의 uuid, email, nickname을 제공하는 기본 프로필 정보 구조.
 * - 프로필 조회 및 닉네임/이메일 변경 후 변경된 값을 반환할 때 사용된다.
 *
 * 🔎 사용되는 Controller API:
 * 1) MemberProfileController.updateNickname()
 *    → PUT /api/auth/members/nickname
 *
 * 2) MemberProfileController.updateEmail()
 *    → PUT /api/auth/members/email
 *
 * 3) MemberProfileController.getProfile(uuid)
 *    → GET /api/auth/members/{uuid}/profile
 *
 * 서비스 레이어에서도 사용되지만, 문서화 대상은 Controller이므로
 * 외부로 공개되는 실제 API는 위 3개이다.
 */
@Schema(
        description = """
                회원 프로필 응답 DTO.
                
                사용 API:
                - PUT /api/auth/members/nickname
                - PUT /api/auth/members/email
                - GET /api/auth/members/{uuid}/profile

                uuid, email, nickname을 포함한 기본 프로필 정보이며,
                사용자 프로필 조회 및 프로필 수정 후 갱신된 데이터를 반환할 때 사용된다.
                """
)
public record MemberProfileResponse(

        @Schema(description = "회원 UUID", example = "f391d23e-13af-4a63-bb8e-91c4b8df112e")
        String uuid,

        @Schema(description = "회원 이메일", example = "tiger@example.com")
        String email,

        @Schema(description = "회원 닉네임", example = "행복한호랑이")
        String nickname
) {}

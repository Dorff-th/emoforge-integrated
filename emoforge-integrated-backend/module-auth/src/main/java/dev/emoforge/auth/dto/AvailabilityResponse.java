package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 중복 체크 응답 DTO
 * - 닉네임 또는 이메일이 사용 가능한지 여부를 Boolean 값으로 전달한다.
 *
 * 🔎 사용되는 API (Controller 기준):
 * - MemberProfileController.checkNickname()
 *   → GET /api/auth/members/check-nickname?nickname={nickname}
 *
 * - MemberProfileController.checkEmail()
 *   → GET /api/auth/members/check-email?email={email}
 *
 * 서비스 단에서도 활용되지만 문서화 대상은 Controller이므로,
 * 실제 외부에 노출되는 API는 위 두 엔드포인트이다.
 */
@Schema(
        description = """
                중복 체크 응답 DTO.
                
                사용되는 API:
                - GET /api/auth/members/check-nickname
                - GET /api/auth/members/check-email

                available 값이 true이면 사용 가능,
                false이면 이미 사용 중인 값이다.
                """
)
public record AvailabilityResponse(

        @Schema(
                description = "사용 가능 여부 (true = 사용 가능, false = 이미 존재)",
                example = "true"
        )
        boolean available
) {}

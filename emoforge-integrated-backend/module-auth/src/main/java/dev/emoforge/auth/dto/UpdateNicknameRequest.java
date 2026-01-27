package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 닉네임 변경 요청 DTO.
 * - 사용자가 자신의 닉네임을 변경할 때 전달하는 요청 구조.
 *
 * 🔎 사용되는 Controller API:
 * - MemberProfileController.updateNickname()
 *   → PUT /api/auth/members/nickname
 *
 * 서비스 레이어에서도 사용되지만,
 * 문서화 기준은 Controller이므로 외부로 노출되는 API는 위 1개이다.
 */
@Schema(
        description = """
                닉네임 변경 요청 DTO.

                사용 API:
                - PUT /api/auth/members/nickname

                nickname 필드는 사용자가 새로 설정하려는 닉네임을 의미한다.
                중복 여부는 별도 /check-nickname API에서 확인한다.
                """
)
public record UpdateNicknameRequest(
        @Schema(
                description = "변경할 새로운 닉네임",
                example = "행복한호랑이"
        )
        String nickname
) {}
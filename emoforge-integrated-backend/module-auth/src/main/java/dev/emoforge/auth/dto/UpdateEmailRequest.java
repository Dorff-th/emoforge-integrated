package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 이메일 변경 요청 DTO.
 * - 사용자가 자신의 이메일을 변경할 때 전달하는 요청 구조.
 *
 * 🔎 사용되는 Controller API:
 * - MemberProfileController.updateEmail()
 *   → PUT /api/auth/members/email
 *
 * 이메일 중복 여부는 /check-email API에서 별도로 확인하며,
 * 이 DTO는 실제 변경 요청 시 사용된다.
 */
@Schema(
        description = """
                이메일 변경 요청 DTO.

                사용 API:
                - PUT /api/auth/members/email

                email 필드는 사용자가 새로 설정하려는 이메일 주소를 의미한다.
                중복 체크는 /api/auth/members/check-email 에서 수행된다.
                """
)
public record UpdateEmailRequest(

        @Schema(
                description = "변경할 새로운 이메일 주소",
                example = "new-email@example.com"
        )
        String email
) {}


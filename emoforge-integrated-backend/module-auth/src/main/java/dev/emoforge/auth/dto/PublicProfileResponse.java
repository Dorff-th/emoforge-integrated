// dev.emoforge.auth.dto.PublicProfileResponse.java
package dev.emoforge.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공개용 사용자 프로필 응답 DTO.
 *
 * - 닉네임 + 프로필 이미지 URL만 외부에 공개할 때 사용하는 응답 구조.
 * - 민감 정보를 포함하지 않는 경량 DTO이며,
 *   주로 사용자 프로필 공개 페이지 / 게시글 작성자 정보 표시 등에 활용될 수 있다.
 *
 * 🔎 사용되는 API (Controller 기준):
 * - PublicProfileController.getPublicProfile()
 *   → GET /api/auth/public/members/{uuid}/profile
 */
@Schema(
        description = """
                공개용 사용자 프로필 응답 DTO.

                사용 API:
                - GET /api/auth/public/members/{uuid}/profile

                닉네임과 프로필 이미지 URL만 제공되는 경량 공개 프로필 정보이다.
                """
)
public record PublicProfileResponse(

        @Schema(
                description = "공개용 닉네임",
                example = "행복한호랑이"
        )
        String nickname,

        @Schema(
                description = "프로필 이미지 URL (없을 경우 null)",
                example = "https://cdn.emoforge.dev/profile/abc123.png",
                nullable = true
        )
        String profileImageUrl
) {}

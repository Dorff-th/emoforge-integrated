package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 게시글 생성 요청 DTO.
 *
 * 사용자가 게시글 작성 화면에서 입력한 제목, 내용, 카테고리, 태그 등을
 * 서버로 전달할 때 사용되는 RequestBody 구조이다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.createPost()
 *    → POST /api/posts
 *
 * 사용 흐름:
 * Post-Frontend에서 사용자가 제목/내용/카테고리를 입력하고 저장 버튼을 누르면
 * JSON 형태로 본 DTO가 전송되며,
 * PostService.createPost()로 전달되어 실제 게시글이 생성된다.
 *
 * 필드 구성:
 * - title: 게시글 제목 (필수)
 * - content: 게시글 내용 (필수)
 * - categoryId: 선택한 카테고리 ID (필수)
 * - tags: 콤마 기반의 태그 문자열 (예: "spring,java,boot")
 *
 * memberUuid는 인증 정보에서 추출되므로 RequestDTO에서는 받지 않는다.
 */
@Schema(
    description = """
                게시글 생성 요청 DTO.

                사용 API:
                - POST /api/posts

                게시글 작성 화면에서 입력한 제목, 내용, 카테고리, 태그 정보를 포함하며,
                memberUuid는 JWT 인증에서 자동 추출되므로 포함되지 않는다.
                """
)
@Builder
public record PostRequestDTO(

        @Schema(description = "게시글 제목", example = "Spring Boot에서 JWT 인증 구현하기")
        @NotBlank(message = "{NotBlankPostTitle}") String title,

        @Schema(description = "게시글 내용 (HTML 또는 Markdown 가능)",
            example = "<p>JWT 인증은 다음과 같이 구현합니다...</p>")
        @NotBlank(message = "{NotBlankPostContent}") String content,

        @Schema(description = "카테고리 ID", example = "3")
        @NotNull Long categoryId,

        @Schema(
            description = """
                        태그 목록(콤마 구분 문자열).
                        예: "spring,boot,jwt"
                        """,
            example = "spring,boot,jwt"
        )
        String tags

) {
}

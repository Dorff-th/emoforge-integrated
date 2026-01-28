package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 게시글 수정 요청 DTO.
 *
 * 게시글 수정 화면에서 사용자가 변경한 제목, 내용, 카테고리, 태그 등을
 * 서버로 전달할 때 사용하는 RequestBody 구조이다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.updatePost()
 *    → PUT /api/posts/{id}
 *
 * 사용 흐름:
 * - Post-Frontend에서 기존 게시글을 불러온 후 수정된 내용으로 본 DTO를 생성하여 요청
 * - authorUuid는 프론트에서 hidden 필드로 전달되며,
 *   서버에서는 Authentication 정보를 검증하여 실제 작성자와 일치하는지 권한 체크
 * - tags: 신규로 추가되는 태그 문자열 (콤마 기반)
 * - deleteTagIds: 삭제될 태그의 ID 목록 (콤마 기반)
 *
 * 기본 동작:
 * - updatedAt 값이 null이면 서버에서 LocalDateTime.now()로 자동 설정
 */
@Schema(
    description = """
                게시글 수정 요청 DTO.

                사용 API:
                - PUT /api/posts/{id}

                수정 가능한 항목:
                - 제목(title)
                - 내용(content)
                - 카테고리(categoryId)
                - 태그(tags, deleteTagIds)

                authorUuid는 작성자 본인 여부 검증을 위해 전달되며,
                updatedAt이 null일 경우 서버에서 자동으로 현재 시각(LocalDateTime.now)으로 설정된다.
                """
)
@Builder
public record PostUpdateDTO(

        @Schema(description = "수정할 게시글 ID", example = "42")
        @NotNull Long id,

        @Schema(description = "게시글 제목", example = "Spring Boot JWT 구조 전체 리팩토링")
        @NotBlank(message = "{NotBlankPostTitle}") String title,

        @Schema(description = "게시글 내용 (HTML 또는 Markdown)",
            example = "<p>JWT 필터 구조를 개선한 이유는...</p>")
        @NotBlank(message = "{NotBlankPostContent}") String content,

        @Schema(description = "선택된 카테고리 ID", example = "5")
        @NotNull Long categoryId,

        @Schema(
            description = """
                        게시글 작성자의 UUID.
                        프론트에서는 hidden input 으로 보내며,
                        서버에서는 Authentication의 UUID와 비교하여 작성자 본인 여부를 체크한다.
                        """,
            example = "52fa880e-7344-4c0f-bb71-02f6b07a9311"
        )
        String authorUuid,

        @Schema(
            description = """
                        새로 추가되는 태그 목록(콤마 기반 문자열).
                        예: "cloud,k8s,msa"
                        """,
            example = "msa,cloud"
        )
        String tags, // hidden input "tags"  (신규 입력 tag 들)

        @Schema(
            description = """
                        수정 과정에서 삭제되는 태그의 ID 목록(콤마 기반).
                        예: "12,18"
                        """,
            example = "12,18"
        )
        String deleteTagIds,  // 삭제 대상 tag id 들

        @Schema(
            description = "수정 시각. null이면 서버에서 자동으로 현재 시각으로 설정됨.",
            example = "2025-11-18T09:12:44"
        )
        LocalDateTime updatedAt
) {
    public PostUpdateDTO {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
}

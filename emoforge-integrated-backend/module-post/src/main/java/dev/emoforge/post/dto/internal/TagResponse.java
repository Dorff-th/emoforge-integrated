package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 게시글에 포함된 태그 정보를 반환하는 응답 DTO.
 *
 * PostController.getPostTags()에서 사용되며,
 * 특정 게시글에 등록된 태그 목록을 단순한 id + name 구조로 제공한다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.getPostTags()
 *    → GET /api/posts/{id}/tags
 *
 * 용도:
 * - 게시글 상세 화면(PostDetail)에서 태그 UI 표시
 * - 목록 또는 검색 기능과 결합하여 태그 필터링 가능
 */
@Schema(
    description = """
                게시글 태그 응답 DTO.

                사용 API:
                - GET /api/posts/{id}/tags

                태그 ID와 태그명을 반환하는 단순 구조로,
                게시글 상세 화면에서 태그 표시를 위해 사용된다.
                """
)
@Builder
public record TagResponse(

        @Schema(description = "태그 ID", example = "7")
        Long id,

        @Schema(description = "태그 이름", example = "spring")
        String name
) {
}

package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 게시글 목록 조회 시 페이징 및 정렬 조건을 전달하기 위한 요청 DTO.
 *
 * page, size, sort, direction 값을 클라이언트가 전달하면
 * 자동으로 기본값 보정 및 Pageable 변환 기능을 제공한다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.getPostList()
 *    → GET /api/posts
 *
 * 2) PostController.getPostListByTag()
 *    → GET /api/posts/tags/{tagName}
 *
 * 기본 동작:
 * - page < 1 → page = 1
 * - size <= 0 → size = 10
 * - sort가 null/빈 문자열 → "id"
 * - direction이 null → DESC
 *
 * 또한 page 값을 PageRequest.of()에 맞게 1 감소시켜 내부적으로 0-based index 적용한다.
 */
@Schema(
    description = """
                페이징 및 정렬 요청 DTO.

                사용 API:
                - GET /api/posts
                - GET /api/posts/tags/{tagName}

                제공 기능:
                - 페이지 번호(page), 페이지 크기(size)
                - 정렬 기준(sort) 및 방향(direction)
                - 기본값 자동 보정 로직 내장
                - Pageable로 변환 가능
                """
)
@Builder
public record PageRequestDTO(

        @Schema(description = "페이지 번호 (1부터 시작)", example = "1")
        int page,

        @Schema(description = "페이지 크기 (기본값: 10)", example = "10")
        int size,

        @Schema(description = "정렬 기준 필드명 (기본값: id)", example = "createdAt")
        String sort,

        @Schema(
            description = "정렬 방향 (ASC 또는 DESC)",
            example = "DESC"
        )
        SortDirection direction
) {
    public PageRequestDTO {
        if (page < 1) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        if (sort == null || sort.isBlank()) {
            sort = "id";
        }
        if (direction == null) {
            direction = SortDirection.DESC;
        }
    }

    public Pageable toPageable() {
        return PageRequest.of(page - 1, size, Sort.by(direction.toSpringDirection(), sort));
    }

    public int offset() {
        return (page - 1) * size;
    }
}

package dev.emoforge.post.dto.internal;

import java.time.LocalDateTime;

/**
 * 게시글 검색 쿼리 결과 DTO.
 *
 * PostSearchQueryService에서 반환하는 순수 DB 조회 결과.
 * Auth/Attach 등 외부 서비스 정보는 포함하지 않는다.
 *
 * MyBatis SearchMapper에서 직접 매핑되며,
 * Facade 계층에서 외부 서비스 정보와 조합하여 최종 Response를 생성한다.
 *
 * 사용처:
 * - PostSearchQueryService.searchPosts()
 * - SearchMapper.searchPostsForAdmin()
 */
public record PostSearchResultDTO(

    /** 게시글 ID */
    Long id,

    /** 게시글 제목 */
    String title,

    /** 게시글 본문 (필요 시 미리보기용) */
    String content,

    /** 작성일시 */
    LocalDateTime createdAt,

    /** 작성자 UUID */
    String memberUuid,

    /** 카테고리 ID */
    Long categoryId,

    /** 카테고리 이름 */
    String categoryName,

    /** 댓글 개수 (서브쿼리로 조회) */
    Long commentCount

) {}

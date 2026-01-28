package dev.emoforge.post.dto.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시글 상세정보 조회용 내부 DTO.
 *
 * ✔ Controller에서 직접 사용되지 않으며, 외부 API로 노출되지 않는 internal 전용 DTO이다.
 * ✔ Service 계층(PostDetailFacadeService 등)에서 게시글 상세 조회 시 사용되며,
 *    Repository 계층의 맞춤 조회 결과를 담기 위한 데이터 구조로 사용된다.
 *
 * 🔎 사용되는 Repository 메서드:
 * - PostRepository.findPostDetail()
 *
 * 주 용도:
 * - 게시글 상세정보(id, 제목, 내용, 작성자, 카테고리 정보 등)를
 *   JPA 커스텀 조회 또는 JPQL/QueryDSL 매핑 결과로 받아오기 위한 구조체 역할.
 *
 * 주의:
 * - 외부 API 문서(Swagger) 대상 아님.
 * - BFF 응답(PostDetailResponse)로 변환되는 중간 내부 모델.
 */
@NoArgsConstructor
//@Builder
@Data
public class PostDetailDTO {
    private Long id;
    private String memberUuid;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private String categoryName;

    public PostDetailDTO(Long id, String memberUuid, String title, String content, LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         Long categoryId, String categoryName) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }
}

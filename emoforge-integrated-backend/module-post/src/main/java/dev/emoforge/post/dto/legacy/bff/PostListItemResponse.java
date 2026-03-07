package dev.emoforge.post.dto.legacy.bff;

import dev.emoforge.post.admin.dto.AdminPostListItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 게시글 목록 화면에서 사용하는 단일 게시글 요약 응답 DTO (BFF).
 *
 * PostController.getPostList(), PostController.getPostListByTag()에서 반환되는
 * PageResponseDTO<PostListItemResponse>의 단일 요소로,
 * 프론트의 목록(UI) 렌더링에 필요한 정보를 모두 담고 있다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.getPostList()
 *    → GET /api/posts
 *
 * 2) PostController.getPostListByTag()
 *    → GET /api/posts/tags/{tagName}
 *
 * 데이터 구성 (BFF):
 * - PostService(DB): id, title, createdAt, categoryName
 * - Auth-Service: nickname, profileImageUrl (작성자 정보)
 * - Attach-Service: attachmentCount (일반 첨부파일 개수)
 * - CommentService: commentCount (댓글 개수)
 *
 * 주요 용도:
 * - 게시글 목록 및 태그 기반 목록 화면에서 리스트 아이템 형태로 렌더링
 * - 상세 페이지 이동 없이 기본 정보를 표시할 때 사용
 */
@Schema(
    description = """
                게시글 목록 응답 DTO(BFF).

                사용 API:
                - GET /api/posts
                - GET /api/posts/tags/{tagName}

                게시글 목록 화면에서 필요한 핵심 정보(제목, 작성자, 카테고리, 댓글 수, 첨부 수 등)를
                담고 있으며, PageResponseDTO 내부 dtoList로 전달된다.
                """
)
@Builder
public record PostListItemResponse(

    @Schema(description = "게시글 ID", example = "42")
    Long postId,
    //Long id,

    @Schema(description = "게시글 제목", example = "Spring Boot로 JWT 인증 구현하기")
    String title,

    @Schema(description = "작성일시", example = "2025-11-18T09:10:22")
    LocalDateTime createdAt,

    @Schema(description = "카테고리 이름", example = "Spring")
    String categoryName,

    // ---------- Auth-Service BFF 영역 ----------
    @Schema(description = "작성자 닉네임", example = "행복한호랑이")
    String nickname,

    @Schema(description = "작성자 프로필 이미지 URL",
        example = "https://cdn.emoforge.dev/profile/aaa123.png")
    String profileImageUrl,  // ✅ 추가

    // ---------- Comment/Attach BFF 영역 ----------
    @Schema(description = "댓글 개수", example = "12")
    Long commentCount,

    @Schema(description = "첨부파일 개수", example = "3")
    int attachmentCount

) {
    //2026.03.7 추가
    public static PostListItemResponse fromAdminDTO(AdminPostListItemDTO dto) {
        return new PostListItemResponse(
                dto.id(),
                dto.title(),
                dto.createdAt(),
                dto.categoryName(),
                dto.nickname(),
                null,
                null,
                0
        );
    }
}

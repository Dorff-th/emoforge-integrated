package dev.emoforge.post.dto.legacy.bff;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 댓글 상세 응답 DTO (BFF).
 *
 * CommentController.getCommentsByPostId()에서 사용되며,
 * 단순 댓글 정보(Comment 엔티티) 뿐만 아니라
 * 작성자 정보(Auth-Service의 nickname, profileImageUrl)까지 조합된
 * BFF(Backend For Frontend) 전용 응답 구조이다.
 *
 * 🔎 사용되는 Controller API:
 * 1) CommentController.getCommentsByPostId()
 *    → GET /api/posts/{postId}/comments
 *
 * 데이터 구성:
 * - id, postId, memberUuid, content, createdAt: Comment 엔티티 기반
 * - nickname, profileImageUrl: Auth-Service → CommentsFacadeService에서 합성
 *
 * 주요 용도:
 * - 게시글 상세 화면(PostDetail)에서 댓글 목록 표시
 * - 작성자 프로필 이미지와 닉네임을 함께 노출해야 하므로 단일 DTO로 통합 제공
 */
@Schema(
    description = """
                댓글 상세 응답 DTO(BFF 조합).

                사용 API:
                - GET /api/posts/{postId}/comments

                댓글(Comment) 기본 정보 + 작성자(Auth-Service) 정보가 합쳐진
                BFF 전용 구조로, 댓글 목록 화면에서 사용된다.
                """
)
public record CommentDetailResponse(

        @Schema(description = "댓글 ID", example = "101")
        Long id,

        @Schema(description = "댓글이 속한 게시글 ID", example = "42")
        Long postId,

        @Schema(description = "댓글 작성자 UUID", example = "ff1e7f23-cc99-47dd-8c10-a6a82e77c244")
        String memberUuid,

        @Schema(description = "댓글 내용", example = "정말 유익한 글이네요! 감사합니다 🙏")
        String content,

        @Schema(description = "댓글 작성 시각", example = "2025-11-18T08:44:21")
        LocalDateTime createdAt,

        // -------- BFF 합성 영역 --------
        @Schema(description = "댓글 작성자의 닉네임", example = "행복한호랑이")
        String nickname,

        @Schema(description = "댓글 작성자의 프로필 이미지 URL",
            example = "https://www.emoforge.dev/api/attach/profile/abcd1234.png")
        String profileImageUrl
) {
}

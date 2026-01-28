package dev.emoforge.post.dto.internal;

import dev.emoforge.post.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 댓글 작성 후 반환되는 응답 DTO.
 *
 * 사용자가 댓글을 작성하면 CommentController.createComment()에서
 * 저장된 댓글 데이터를 기반으로 CommentResponse를 생성하여 반환한다.
 *
 * 🔎 사용되는 Controller API:
 * 1) CommentController.createComment()
 *    → POST /api/posts/{postId}/comments
 *
 * 반환되는 필드 구성:
 * - id: 생성된 댓글 ID
 * - content: 댓글 내용
 * - createdAt: 작성 시각
 * - postId: 댓글이 달린 게시글 ID
 * - memberUuid: 댓글 작성자의 UUID
 */
@Schema(
    description = """
                댓글 작성 응답 DTO.

                사용 API:
                - POST /api/posts/{postId}/comments

                댓글 저장 직후 반환되며,
                댓글 ID, 내용, 작성 시각, 게시글 ID, 작성자 UUID를 포함한다.
                """
)
@Builder
public record CommentResponse(

        @Schema(description = "댓글 ID", example = "1024")
        Long id,

        @Schema(description = "댓글 내용", example = "좋은 글 감사합니다! 😊")
        String content,

        @Schema(description = "댓글 작성 시각 (KST 기준)", example = "2025-11-18T08:55:12")
        LocalDateTime createdAt,

        @Schema(description = "댓글이 속한 게시글 ID", example = "88")
        Long postId,

        @Schema(description = "댓글 작성자 UUID", example = "f2b7fbd3-94e2-4d2e-a885-7de2ea1c21bb")
        String memberUuid
) {
    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .postId(comment.getPostId())
                .memberUuid(comment.getMemberUuid())
                .build();
    }
}

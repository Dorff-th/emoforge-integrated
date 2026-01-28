package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 작성 요청 DTO.
 *
 * 사용자가 게시글 상세 화면에서 새로운 댓글을 입력할 때,
 * CommentController.createComment()의 RequestBody로 전달되는 구조이다.
 *
 * 🔎 사용되는 Controller API:
 * 1) CommentController.createComment()
 *    → POST /api/posts/{postId}/comments
 *
 * 프론트엔드에서는 댓글 입력창에서 작성한 내용을 JSON 형태로 전달하며,
 * content 필드는 필수 입력값이다.
 */
@Schema(
    description = """
                댓글 작성 요청 DTO.

                사용 API:
                - POST /api/posts/{postId}/comments

                프론트엔드에서 사용자가 작성한 댓글 내용을 서버로 전달하는 입력 모델이며,
                content 필드는 비어 있을 수 없습니다.
                """
)
@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    @Schema(description = "댓글 내용", example = "정말 좋은 글이네요! 감사합니다 🙏")
    @NotBlank(message = "내용 입력은 필수입니다.")
    private String content;

    public CommentRequest(String content) {
        this.content = content;
    }
}

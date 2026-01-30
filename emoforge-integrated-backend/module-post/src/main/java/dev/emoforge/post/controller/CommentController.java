package dev.emoforge.post.controller;


import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.legacy.bff.CommentDetailResponse;
import dev.emoforge.post.dto.internal.CommentRequest;
import dev.emoforge.post.dto.internal.CommentResponse;
import dev.emoforge.post.dto.query.CommentViewDTO;
import dev.emoforge.post.service.legacy.bff.CommentsFacadeService;
import dev.emoforge.post.service.internal.CommentService;
import dev.emoforge.post.service.internal.PostService;
import dev.emoforge.post.service.query.CommentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 특정 게시글(postId)에 대한 댓글 조회/작성/삭제 기능을 제공하는 컨트롤러.
 *
 * 이 API는 Post-Frontend에서 직접 호출되며,
 * 게시글 상세 화면(PostDetail) 로딩 시 댓글 목록을 가져오거나,
 * 로그인된 사용자가 댓글을 작성/삭제할 때 사용된다.
 *
 * ✔ 댓글 조회: 인증 불필요 (게시글이 공개된 상태라면 누구나 조회 가능)
 * ✔ 댓글 작성/삭제: 인증 필요 (JWT 기반 사용자 식별)
 *
 * 내부 처리 흐름(예: 댓글 작성 시):
 * Post-Frontend
 *    → POST /api/posts/{postId}/comments
 *    → 사용자 인증 정보 확인(authentication)
 *    → commentService.createComment()
 *
 * 댓글 목록 조회는 commentsFacadeService가 내부적으로 Comment + Member 정보 등을
 * 조합하여 "댓글 상세 응답(CommentDetailResponse)" 형태로 BFF 응답을 구성한다.
 */
@Tag(
    name = "Comment API",
    description = "게시글(Post) 내 댓글 조회/작성/삭제 기능을 제공하는 API"
)
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    //private final CommentsFacadeService commentsFacadeService;
    private final CommentQueryService commentQueryService;

    @Operation(
        summary = "특정 게시글의 댓글 목록 조회",
        description = """
                    게시글 상세 화면에서 사용되는 댓글 전체 목록을 반환합니다.

                    ✔ 인증 불필요
                    ✔ Comment + Member 정보를 합성한 BFF 응답(CommentDetailResponse) 반환

                    호출 흐름:
                    Post-Frontend → CommentController → CommentsFacadeService

                    반환 예(요약):
                    [
                      {
                        "commentId": 1,
                        "content": "댓글 내용",
                        "author": {
                            "uuid": "...",
                            "nickname": "홍길동",
                            "profileImageUrl": "..."
                        },
                        "createdAt": "...",
                        "updatedAt": "..."
                      }
                    ]
                    """
    )
    @GetMapping
    public ResponseEntity<List<CommentViewDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentViewDTO> comments = commentQueryService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(
        summary = "댓글 작성",
        description = """
                    특정 게시글(postId)에 새로운 댓글을 작성합니다.

                    ✔ 인증 필요 (JWT)
                    ✔ 요청 본문에 댓글 내용(CommentRequest.content) 전달
                    ✔ 작성자 정보는 authentication에서 추출

                    내부 처리 흐름:
                    Post-Frontend
                      → POST /api/posts/{postId}/comments
                      → JWT로 사용자 식별
                      → CommentService.createComment()

                    반환값:
                    - commentId, content, 작성자 정보, createdAt 등의 CommentResponse
                    """
    )
    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequest request,
            Authentication authentication
    ) {

        // 1. 게시글 존재 여부 확인
        Post post = postService.getPostById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();

        CommentResponse response = commentService.createComment(postId, memberUuid, request.getContent());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "댓글 삭제",
        description = """
                    특정 게시글(postId)에 작성된 댓글(commentId)을 삭제합니다.

                    ✔ 인증 필요 (JWT)
                    ✔ 댓글 작성자 본인만 삭제 가능

                    내부 처리 흐름:
                    Post-Frontend
                      → DELETE /api/posts/{postId}/comments/{commentId}
                      → JWT로 사용자 식별
                      → CommentService.deleteComment()

                    응답: HTTP 204 No Content
                    """
    )
    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            Authentication authentication
    ) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();
        commentService.deleteComment(postId, commentId, memberUuid);
        return ResponseEntity.noContent().build();
    }

}

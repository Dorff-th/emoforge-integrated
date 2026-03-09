package dev.emoforge.post.admin.controller;

import dev.emoforge.post.admin.dto.AdminCommentBulkDeleteRequest;
import dev.emoforge.post.admin.dto.AdminCommentSearchType;
import dev.emoforge.post.admin.dto.AdminPostSearchType;
import dev.emoforge.post.admin.service.AdminCommentService;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.query.CommentViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 화면에서 댓글관리는 게시판과 별도로 분리하여 관리
 */
@RestController
@RequestMapping("/api/posts/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @GetMapping
    public ResponseEntity<?> getCommentList(
            PageRequestDTO requestDTO,
            @RequestParam(value = "searchType", required = false, defaultValue = "ALL") AdminCommentSearchType searchType,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {

        return ResponseEntity.ok(adminCommentService.getCommentList(requestDTO, searchType, keyword));
    }

    //not used
    /*public ResponseEntity<List<CommentViewDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {

        List<CommentViewDTO> comments = adminCommentService.findByPostId(postId);

        return ResponseEntity.ok(comments);
    }*/

    /*@DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        adminCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }*/

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteComments(@RequestBody AdminCommentBulkDeleteRequest request) {
        adminCommentService.bulkDeleteComments(request.commentIds());
        return ResponseEntity.noContent().build();
    }
}

package dev.emoforge.post.admin.controller;

import dev.emoforge.post.admin.service.AdminCommentService;
import dev.emoforge.post.dto.query.CommentViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/admin/posts/{postId}/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    public ResponseEntity<List<CommentViewDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {

        List<CommentViewDTO> comments = adminCommentService.findByPostId(postId);

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        adminCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteComments(@RequestBody List<Long> commentIds) {
        adminCommentService.bulkDeleteComments(commentIds);
        return ResponseEntity.noContent().build();
    }
}

package dev.emoforge.post.admin.controller;

import dev.emoforge.post.admin.service.AdminCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

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

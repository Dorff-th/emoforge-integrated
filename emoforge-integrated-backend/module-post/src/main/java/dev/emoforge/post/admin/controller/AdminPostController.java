package dev.emoforge.post.admin.controller;

import dev.emoforge.post.admin.service.AdminPostService;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Admin Post API",
    description = "Admin-only post management API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/admin/posts")
public class AdminPostController {

    private final AdminPostService adminPostService;

    @Operation(summary = "List posts")
    @GetMapping
    public ResponseEntity<?> getPostList(
        PageRequestDTO requestDTO,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ResponseEntity.ok(adminPostService.getPostList(requestDTO, keyword));
    }

    @Operation(summary = "Get post detail")
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> getPostDetail(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(adminPostService.getPostDetail(id));
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        adminPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bulk delete posts")
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeletePosts(@RequestBody List<Long> postIds) {
        adminPostService.bulkDeletePosts(postIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update post by admin")
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<?> updatePost(
        Authentication authentication,
        @PathVariable("id") Long id,
        @RequestBody PostUpdateDTO request
    ) {
        String adminUsername = authentication.getName();
        return ResponseEntity.ok(adminPostService.updatePost(id, request, adminUsername));
    }
}

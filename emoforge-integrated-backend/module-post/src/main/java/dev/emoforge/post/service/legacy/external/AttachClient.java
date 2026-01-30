package dev.emoforge.post.service.legacy.external;

import dev.emoforge.post.dto.legacy.external.AttachmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "attach-service", url = "${service.attach.url}")
public interface AttachClient {

    @PostMapping("/api/attach/count/batch")
    Map<Long, Integer> countByPostIds(@RequestBody List<Long> postIds);
    @GetMapping("/api/attach/post/{postId}")
    List<AttachmentResponse> findByPostId(@PathVariable("postId") Long postId, @RequestParam("uploadType") String uploadType);

    @GetMapping("/api/attach/profile-images/{memberUuid}")
    public ResponseEntity<AttachmentResponse> findLatestProfileImage(@PathVariable("memberUuid") String memberUuid);

    @DeleteMapping("/api/attach/post/{postId}")
    public ResponseEntity<Void> deleteByPostId(@PathVariable("postId") Long postId);


}

package dev.emoforge.post.service.legacy.external;

import dev.emoforge.post.dto.legacy.external.AttachmentResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Primary
public class PostDummyAttachClient implements AttachClient{
    @Override
    public Map<Long, Integer> countByPostIds(List<Long> postIds) {
        return null;
    }

    @Override
    public List<AttachmentResponse> findByPostId(Long postId, String uploadType) {
        return null;
    }

    @Override
    public ResponseEntity<AttachmentResponse> findLatestProfileImage(String memberUuid) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteByPostId(Long postId) {
        return null;
    }
}

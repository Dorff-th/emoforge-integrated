package dev.emoforge.post.service.legacy.bff;

import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.repository.PostTagRepository;
import dev.emoforge.post.service.legacy.external.AttachClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostDeleteFacadeService {

    private final PostRepository postRepository;
    private final AttachClient attachClient;
    private final CommentRepository commentRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        postTagRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
        commentRepository.deleteByPostId(postId);

        try {
            attachClient.deleteByPostId(postId); // ✅ Feign으로 첨부 삭제 호출
        } catch (Exception e) {
            throw new RuntimeException("첨부 삭제 실패 → 전체 롤백", e);
        }
    }

}

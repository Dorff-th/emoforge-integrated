package dev.emoforge.post.service.internal;


import dev.emoforge.post.domain.Comment;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.internal.CommentResponse;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    //private final MemberRepository memberRepository;

    // 목록기능 - CommentsFacadeSerice 참조

    @Transactional
    public CommentResponse createComment(Long postId, String memberUuid, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = Comment.create(postId, memberUuid, content);

        Comment saved = commentRepository.save(comment);
        return CommentResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, String memberUuid) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시물의 댓글이 아닙니다.");
        }
        if (!comment.getMemberUuid().equals(memberUuid)) {
            throw new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

}

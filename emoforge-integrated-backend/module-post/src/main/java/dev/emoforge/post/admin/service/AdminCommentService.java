package dev.emoforge.post.admin.service;

import dev.emoforge.post.dto.query.CommentViewDTO;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.service.query.CommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentService {

    private final CommentRepository commentRepository;
    private final CommentQueryService commentQueryService;

    public List<CommentViewDTO> findByPostId(Long postId) {
        return commentQueryService.findByPostId(postId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void bulkDeleteComments(List<Long> commentIds) {
        commentRepository.deleteAllById(commentIds);
    }
}

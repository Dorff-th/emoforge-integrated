package dev.emoforge.post.admin.service;

import dev.emoforge.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void bulkDeleteComments(List<Long> commentIds) {
        commentRepository.deleteAllById(commentIds);
    }
}

package dev.emoforge.post.service.query;


import dev.emoforge.post.dto.query.CommentViewDTO;
import dev.emoforge.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public List<CommentViewDTO> findByPostId(Long postId) {
        return commentRepository.findCommentsByPostId(postId)
                .stream()
                .map(CommentViewDTO::from)
                .toList();
    }
}

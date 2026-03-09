package dev.emoforge.post.admin.service;

import dev.emoforge.post.admin.dto.AdminCommentListItemResponse;
import dev.emoforge.post.admin.dto.AdminCommentSearchType;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import dev.emoforge.post.dto.legacy.bff.PostListItemResponse;
import dev.emoforge.post.dto.query.CommentViewDTO;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.service.query.CommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentService {

    private final CommentRepository commentRepository;
    private final CommentQueryService commentQueryService;
    private static final int PAGE_BLOCK_SIZE = 10;


    public PageResponseDTO<AdminCommentListItemResponse> getCommentList(PageRequestDTO requestDTO,
                                                                        AdminCommentSearchType searchType,
                                                                        String keyword
    ) {

        String normalizedKeyword = (keyword == null) ? null : keyword.trim();
        if (normalizedKeyword != null && normalizedKeyword.isEmpty()) {
            normalizedKeyword = null;
        }
        Page<AdminCommentListItemResponse> result = commentRepository.findAdminComments(searchType.name(), normalizedKeyword, requestDTO.toPageable());


        PageResponseDTO pageResponseDTO = new PageResponseDTO(requestDTO, result.getTotalElements(), result.getContent(), requestDTO.size());

        return pageResponseDTO;


    }

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

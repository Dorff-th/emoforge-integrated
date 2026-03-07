package dev.emoforge.post.admin.service;

import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.post.admin.dto.AdminPostListItemDTO;
import dev.emoforge.post.admin.dto.AdminPostSearchType;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostDetailResponse;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import dev.emoforge.post.dto.legacy.bff.PostListItemResponse;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.repository.PostTagRepository;
import dev.emoforge.post.service.internal.PostService;
import dev.emoforge.post.service.query.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostService {
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;
    private final PostQueryService postQueryService;
    private final PostService postService;
    private static final int PAGE_BLOCK_SIZE = 10;

    public PageResponseDTO<PostListItemResponse> getPostList(
        PageRequestDTO requestDTO,
        AdminPostSearchType searchType,
        String keyword
    ) {
        String normalizedKeyword = (keyword == null) ? null : keyword.trim();
        if (normalizedKeyword != null && normalizedKeyword.isEmpty()) {
            normalizedKeyword = null;
        }

        Page<AdminPostListItemDTO> result =
            postRepository.findAdminPostList(searchType.name(), normalizedKeyword, requestDTO.toPageable());

        List<PostListItemResponse> dtoList =
                result.getContent().stream()
                        .map(PostListItemResponse::fromAdminDTO)
                        .toList();

        PageResponseDTO pageResponseDTO = new PageResponseDTO(requestDTO, result.getTotalElements(), dtoList, PAGE_BLOCK_SIZE);

        return pageResponseDTO;
    }

    public PostDetailResponse getPostDetail(Long id) throws NotFoundException {
        return postQueryService.getPostDetail(id);
    }

    public void deletePost(Long id) {
        postService.deletePost(id);
    }

    @Transactional
    public void bulkDeletePosts(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return;
        }

        List<Long> uniquePostIds = postIds.stream().distinct().toList();

        // FK 제약 순서대로 자식 데이터부터 삭제
        postTagRepository.deleteByPostIdIn(uniquePostIds);
        commentRepository.deleteByPostIdIn(uniquePostIds);
        attachmentRepository.deleteByPostIdIn(uniquePostIds);
        postRepository.deleteByIdIn(uniquePostIds);
    }

    @Transactional
    public Long updatePost(Long id, PostUpdateDTO request, String adminUsername) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found. id=" + id));

        post.updateByAdmin(request.title(), request.content(), adminUsername);

        return post.getId();
    }
}

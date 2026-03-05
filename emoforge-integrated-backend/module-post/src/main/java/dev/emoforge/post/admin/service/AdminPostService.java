package dev.emoforge.post.admin.service;

import dev.emoforge.post.admin.dto.AdminPostListItemDTO;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostDetailResponse;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import dev.emoforge.post.repository.PostRepository;
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
    private final PostQueryService postQueryService;
    private final PostService postService;

    public Page<AdminPostListItemDTO> getPostList(PageRequestDTO requestDTO, String keyword) {
        return postRepository.findAdminPostList(keyword, requestDTO.toPageable());
    }

    public PostDetailResponse getPostDetail(Long id) throws NotFoundException {
        return postQueryService.getPostDetail(id);
    }

    public void deletePost(Long id) {
        postService.deletePost(id);
    }

    @Transactional
    public void bulkDeletePosts(List<Long> postIds) {
        for (Long postId : postIds) {
            postService.deletePost(postId);
        }
    }

    @Transactional
    public Long updatePost(Long id, PostUpdateDTO request, String adminUsername) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found. id=" + id));

        post.updateByAdmin(request.title(), request.content(), adminUsername);

        return post.getId();
    }
}

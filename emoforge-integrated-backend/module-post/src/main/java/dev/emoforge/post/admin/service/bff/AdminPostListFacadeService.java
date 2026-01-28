package dev.emoforge.post.admin.service.bff;

import dev.emoforge.post.admin.dto.bff.AdminPageResponseDTO;
import dev.emoforge.post.admin.dto.bff.AdminPostListItemDTO;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSimpleDTO;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.service.external.AttachClient;
import dev.emoforge.post.service.external.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminPostListFacadeService {

    private static final int PAGE_BLOCK_SIZE = 10;

    private final PostRepository postRepository;
    private final AuthClient authClient;
    //private final CommentClient commentClient;
    private final AttachClient attachClient;

    public AdminPageResponseDTO<AdminPostListItemDTO> getPostList(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
            requestDTO.page() - 1,
            requestDTO.size(),
            Sort.by(Sort.Direction.valueOf(requestDTO.direction().toString()), requestDTO.sort())
        );

        Page<PostSimpleDTO> posts =  postRepository.findAllPosts(pageable);


        List<PostSimpleDTO> dtoList = posts.getContent();
        if (dtoList.isEmpty()) {
            return new AdminPageResponseDTO<>(requestDTO, posts.getTotalElements(), List.of(), PAGE_BLOCK_SIZE);
        }

        List<Long> postIds = dtoList.stream().map(PostSimpleDTO::id).toList();
        Map<Long, Integer> attachCounts = postIds.isEmpty()
            ? Collections.emptyMap()
            : attachClient.countByPostIds(postIds);

        Map<String, AuthClient.PublicProfileResponse> profileCache = new HashMap<>();

        List<AdminPostListItemDTO> responses = dtoList.stream()
            .map(post -> {
                AuthClient.PublicProfileResponse profile = profileCache.computeIfAbsent(
                    post.memberUuid(),
                    uuid -> {
                        try {
                            return authClient.getPublicMemberProfile(uuid);
                        } catch (Exception e) {
                            // 비로그인 상태나 Auth 호출 실패 시 fallback
                            return new AuthClient.PublicProfileResponse("익명", null);
                        }
                    }
                );

                return new AdminPostListItemDTO(
                    post.id(),
                    post.title(),
                    post.createdAt(),
                    post.categoryName(),
                    profile.nickname(),
                    post.commentCount(),
                    attachCounts.getOrDefault(post.id(), 0)
                );
            })
            .toList();

        return new AdminPageResponseDTO<>(requestDTO, posts.getTotalElements(), responses, PAGE_BLOCK_SIZE);
    }

}

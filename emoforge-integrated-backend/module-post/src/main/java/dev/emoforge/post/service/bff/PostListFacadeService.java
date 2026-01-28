package dev.emoforge.post.service.bff;

import dev.emoforge.post.dto.bff.PageResponseDTO;
import dev.emoforge.post.dto.bff.PostListItemResponse;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSimpleDTO;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.service.external.AttachClient;
import dev.emoforge.post.service.external.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PostListFacadeService {

    private static final int PAGE_BLOCK_SIZE = 10;

    private final PostRepository postRepository;
    private final AttachClient attachClient;
    private final AuthClient authClient;

    public PageResponseDTO<PostListItemResponse> getPostList(String tagName, PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
            requestDTO.page() - 1,
            requestDTO.size(),
            Sort.by(Sort.Direction.valueOf(requestDTO.direction().toString()), requestDTO.sort())
        );

        Page<PostSimpleDTO> posts = (tagName == null)
            ? postRepository.findAllPosts(pageable)
            : postRepository.findAllPostsByTag(tagName, pageable);

        List<PostSimpleDTO> dtoList = posts.getContent();
        if (dtoList.isEmpty()) {
            return new PageResponseDTO<>(requestDTO, posts.getTotalElements(), List.of(), PAGE_BLOCK_SIZE);
        }

        List<Long> postIds = dtoList.stream().map(PostSimpleDTO::id).toList();

        /*Map<Long, Integer> attachCounts = postIds.isEmpty()
            ? Collections.emptyMap()
            : attachClient.countByPostIds(postIds);*/
        // [2026-01-26] 첨부파일 정보는 선택 데이터이므로, 실패 시 빈 맵으로 대체한다
        Map<Long, Integer> attachCounts =
            postIds.isEmpty()
                ? Collections.emptyMap()
                : fetchAttachCountsSafely(postIds);

        Map<String, AuthClient.PublicProfileResponse> profileCache = new HashMap<>();

        List<PostListItemResponse> responses = dtoList.stream()
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

                return new PostListItemResponse(
                    post.id(),
                    post.title(),
                    post.createdAt(),
                    post.categoryName(),
                    profile.nickname(),
                    profile.profileImageUrl(),
                    post.commentCount(),
                    attachCounts.getOrDefault(post.id(), 0)
                );
            })
            .toList();

        return new PageResponseDTO<>(requestDTO, posts.getTotalElements(), responses, PAGE_BLOCK_SIZE);
    }

    /**
     * 2026-01-26
     * Attachment-Service 호출을 안전하게 감싸는 헬퍼 메서드.
     * 서비스 장애가 발생하더라도 게시글 목록 조회를 중단하지 않도록
     * 실패 시 빈 결과를 반환한다.
     */
    private Map<Long, Integer> fetchAttachCountsSafely(List<Long> postIds) {
        try {
            return attachClient.countByPostIds(postIds);
        } catch (Exception e) {
            log.warn("Attachment-Service unavailable. fallback to empty map", e);
            return Collections.emptyMap();
        }
    }

}

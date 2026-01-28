package dev.emoforge.post.service.bff;

import dev.emoforge.post.domain.Comment;
import dev.emoforge.post.dto.bff.CommentDetailResponse;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.service.external.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsFacadeService {

    private final CommentRepository commentRepository;
    private final AuthClient authClient; // ✅ 이제 Auth만 사용 (Attach 호출 제거)

    /**
     * 특정 게시글의 댓글 목록 조회
     * - Auth-Service의 공개용 프로필 API(/public/members/{uuid}/profile)를 호출
     * - 비로그인 상태에서도 닉네임 + 프로필 이미지 표시 가능
     */
    @Transactional(readOnly = true)
    public List<CommentDetailResponse> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        Map<String, AuthClient.PublicProfileResponse> profileCache = new HashMap<>();

        return comments.stream()
            .map(comment -> {
                String memberUuid = comment.getMemberUuid();

                // ✅ 닉네임 + 프로필 이미지 한 번에 조회 (Auth-Service)
                AuthClient.PublicProfileResponse profile = profileCache.computeIfAbsent(
                    memberUuid,
                    uuid -> {
                        try {
                            return authClient.getPublicMemberProfile(uuid);
                        } catch (Exception e) {
                            log.warn("Failed to load public profile for uuid={}", uuid, e);
                            return new AuthClient.PublicProfileResponse("익명", null);
                        }
                    }
                );

                return new CommentDetailResponse(
                    comment.getId(),
                    comment.getPostId(),
                    comment.getMemberUuid(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    profile.nickname(),
                    profile.profileImageUrl() // ✅ 닉네임 + 이미지 함께 전달
                );
            })
            .toList();
    }
}

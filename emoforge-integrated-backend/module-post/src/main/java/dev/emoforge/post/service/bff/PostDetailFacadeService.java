package dev.emoforge.post.service.bff;

import dev.emoforge.post.dto.bff.PostDetailResponse;
import dev.emoforge.post.dto.external.AttachmentResponse;
import dev.emoforge.post.repository.CommentRepository;
import dev.emoforge.post.repository.PostRepository;
import dev.emoforge.post.service.external.AttachClient;
import dev.emoforge.post.service.external.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDetailFacadeService {

    private final PostRepository postRepository;
    private final AuthClient authClient;   // ✅ 공개 API로 전환
    private final AttachClient attachClient;
    private final CommentRepository commentRepository;

    /**
     * 게시글 상세 조회
     * - Auth-Service: 공개용 프로필 API 사용 (닉네임 + 프로필 이미지)
     * - Attach-Service: 첨부파일 및 에디터 이미지 조회
     */
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) throws NotFoundException {

        // 1️⃣ 게시글 조회
        var postDetailDTO = postRepository.findPostDetail(postId)
            .orElseThrow(() -> new NotFoundException("게시글 없음"));

        // 2️⃣ 작성자 공개 프로필 조회 (비로그인 접근 가능)
        AuthClient.PublicProfileResponse profile;
        try {
            profile = authClient.getPublicMemberProfile(postDetailDTO.getMemberUuid());
        } catch (Exception e) {
            log.warn("Failed to load public profile for uuid={}", postDetailDTO.getMemberUuid(), e);
            profile = new AuthClient.PublicProfileResponse("익명", null);
        }

        // 3️⃣ 첨부파일 조회 (Attachment-Service)
        /*List<AttachmentResponse> editorImages =
            attachClient.findByPostId(postId, "EDITOR_IMAGE");
        List<AttachmentResponse> attachments =
            attachClient.findByPostId(postId, "ATTACHMENT");*/
        List<AttachmentResponse> editorImages =
            fetchAttachmentsSafely(postId, "EDITOR_IMAGE");
        List<AttachmentResponse> attachments =
            fetchAttachmentsSafely(postId, "ATTACHMENT");


        // 4️⃣ DTO 조립 후 반환
        return new PostDetailResponse(
            postDetailDTO.getId(),
            postDetailDTO.getTitle(),
            postDetailDTO.getContent(),
            postDetailDTO.getMemberUuid(),
            postDetailDTO.getCreatedAt(),
            postDetailDTO.getUpdatedAt(),
            postDetailDTO.getCategoryId(),
            postDetailDTO.getCategoryName(),
            profile.nickname(),        // ✅ 공개용 닉네임
            profile.profileImageUrl(), // ✅ 프로필 이미지 URL 추가
            editorImages,
            attachments
        );
    }

    /**
     * 2026-01-26
     * Attachment-Service 호출을 안전하게 감싸는 헬퍼 메서드.
     * 서비스 장애가 발생하더라도 게시글 목록 조회를 중단하지 않도록
     * 실패 시 빈 결과를 반환한다.
     */
    private List<AttachmentResponse> fetchAttachmentsSafely(
        Long postId, String type
    ) {
        try {
            return attachClient.findByPostId(postId, type);
        } catch (Exception e) {
            log.warn("Attachment-Service unavailable. postId={}, type={}", postId, type, e);
            return List.of();
        }
    }
}

package dev.emoforge.post.service.query;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.repository.AttachmentRepository;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostDetailResponse;
import dev.emoforge.post.dto.legacy.external.AttachmentResponse;
import dev.emoforge.post.dto.query.PostDetailViewProjection;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import dev.emoforge.post.dto.query.PostListItemProjection;
import dev.emoforge.post.dto.query.PostListItemSummary;
import dev.emoforge.post.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostQueryService {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

    /**
     * 게시글 목록 조회
     *
     * @param tagName   태그명 (null 또는 blank 가능)
     * @param pageable  페이징 정보
     */

    public Page<PostListItemSummary> getPostList(
            String tagName,
            Long categoryId,
            PageRequestDTO requestDTO
    ) {

        Pageable pageable = PageRequest.of(
                requestDTO.page() - 1,
                requestDTO.size(),
                Sort.by(Sort.Direction.valueOf(requestDTO.direction().toString()), requestDTO.sort())
        );


        // 🔹 tag 조건을 명시적으로 정규화
        String normalizedTag =
                (tagName == null || tagName.isBlank())
                        ? null
                        : tagName;

        Page<PostListItemProjection> posts =
                postRepository.findPostList(normalizedTag, categoryId, pageable);

        // 🔹 Projection → Summary 변환
        return posts.map(PostListItemSummary::from);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) throws NotFoundException {


        // 1️⃣ 게시글 조회
        PostDetailViewProjection view = postRepository.findPostDetailViewById(postId).orElseThrow(() -> new NotFoundException("게시글 없읍니다!"));

        // 2️⃣ 첨부파일 조회
        List<AttachmentResponse> attachments =
                attachmentRepository
                        .findByPostIdAndUploadType(postId, UploadType.ATTACHMENT)
                        .stream()
                        .map(AttachmentResponse::from)
                        .toList();

        return new PostDetailResponse(
                view.getId(),
                view.getTitle(),
                view.getContent(),
                view.getMemberUuid(),
                view.getCreatedAt(),
                view.getUpdatedAt(),
                view.getCategoryId(),
                view.getCategoryName(),
                view.getNickname(),        // ✅ 공개용 닉네임
                null,
                null,
                attachments,
                view.getAdminModifiedAt(),
                view.getAdminModifiedByNickname()
        );
    }

}


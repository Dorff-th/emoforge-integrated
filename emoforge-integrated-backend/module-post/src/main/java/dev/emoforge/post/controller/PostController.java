package dev.emoforge.post.controller;

import dev.emoforge.post.config.CustomUserPrincipal;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.bff.PageResponseDTO;
import dev.emoforge.post.dto.bff.PostDetailResponse;
import dev.emoforge.post.dto.bff.PostListItemResponse;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostRequestDTO;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import dev.emoforge.post.dto.internal.TagResponse;
import dev.emoforge.post.service.bff.PostDeleteFacadeService;
import dev.emoforge.post.service.bff.PostDetailFacadeService;
import dev.emoforge.post.service.bff.PostListFacadeService;
import dev.emoforge.post.service.internal.PostService;
import dev.emoforge.post.service.internal.PostTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글(Post) CRUD 및 목록 조회를 처리하는 메인 컨트롤러.
 *
 * 이 컨트롤러는 Post-Frontend에서 직접 호출되는 핵심 게시판 API로,
 * 아래 기능을 제공한다:
 *
 *  - 게시글 목록 조회 (페이징 + 정렬 + 태그 필터)
 *  - 게시글 상세 조회
 *  - 게시글 작성/수정/삭제
 *  - 게시글에 연결된 태그 목록 조회
 *
 * ✔ 목록/상세 조회는 BFF(Facade) 기반으로 다양한 서비스 응답을 합성 후 제공
 * ✔ 작성/수정/삭제는 인증 필요 (JWT)
 *
 * 내부 BFF 호출 예시:
 *  - 목록 조회: Post-Service(DB) → Attach-Service(썸네일 개수) → Auth-Service(작성자 정보)
 *  - 상세 조회: Post-Service(DB) → Attach-Service(첨부 파일) → Auth-Service(작성자 Profile 포함)
 *
 * API 흐름 예:
 *  Post-Frontend → Post-Service(PostListFacadeService/PostDetailFacadeService)
 */
@Tag(
    name = "Post API",
    description = "게시글 목록/상세 조회, 생성, 수정, 삭제를 담당하는 핵심 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private final PostListFacadeService postListFacadeService;  // bff
    private final PostDetailFacadeService postDetailFacadeService; // bff
    private final PostTagService postTagService;
    private final PostService postService;
    private final PostDeleteFacadeService postDeleteFacadeService; // bbf


    // ============================================================
    // 📌 1. 게시글 목록 조회
    // ============================================================
    @Operation(
        summary = "게시글 목록 조회 (페이징 포함)",
        description = """
                    전체 게시글 목록을 페이징 형태로 반환합니다.

                    ✔ BFF 구조 (여러 서비스 응답 조합)
                      - Post(DB) + Auth(작성자 닉네임/이미지) + Attach(썸네일 이미지 개수)

                    ✔ 검색/정렬/페이지 번호는 PageRequestDTO로 전달

                    호출 흐름:
                    Post-Frontend
                      → GET /api/posts?page=0&size=10&sort=createdAt&direction=DESC
                      → PostListFacadeService
                      → Auth-Service / Attach-Service 내부 호출

                    반환값:
                    - content: 게시글 목록(item DTO)
                    - totalPages, totalElements
                    - 현재 페이지 정보
                    """
    )
    @GetMapping
    public PageResponseDTO<PostListItemResponse> getPostList(PageRequestDTO requestDTO) {
        return postListFacadeService.getPostList(null, requestDTO);
    }

    // ============================================================
    // 📌 2. 특정 태그 기반 게시글 목록 조회
    // ============================================================
    @Operation(
        summary = "특정 태그 기반 게시글 목록 조회",
        description = """
                    특정 태그(tagName)에 포함된 게시글 목록을 페이징 형태로 반환합니다.

                    ✔ 동일하게 BFF 구조
                    ✔ 목록 + 작성자 정보 + 썸네일 개수 포함

                    호출 흐름:
                    Post-Frontend
                      → GET /api/posts/tags/{tagName}
                      → PostListFacadeService
                      → 내부적으로 Post + Tag + Auth + Attach 조합
                    """
    )
    @GetMapping("/tags/{tagName}")
    public PageResponseDTO<PostListItemResponse> getPostListByTag(
        @PathVariable("tagName") String tagName,
        PageRequestDTO requestDTO) {
        return postListFacadeService.getPostList(tagName, requestDTO);
    }

    // ============================================================
    // 📌 3. 게시글 상세 조회
    // ============================================================
    @Operation(
        summary = "게시글 상세 조회",
        description = """
                    게시글 상세 내용을 조회합니다.

                    ✔ BFF 구조
                    ✔ Post + Auth(작성자) + Attach(첨부파일) + Tag 정보 포함
                    ✔ 존재하지 않으면 404(NotFoundException)

                    호출 흐름:
                    Post-Frontend
                      → GET /api/posts/{id}
                      → PostDetailFacadeService
                      → Auth / Attach 내부 호출
                    """
    )
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable("id") Long id) throws NotFoundException {

        PostDetailResponse resultDTO = postDetailFacadeService.getPostDetail(id);

        return ResponseEntity.ok(resultDTO);
    }

    // ============================================================
    // 📌 4. 게시글에 연결된 태그 목록 조회
    // ============================================================
    @Operation(
        summary = "게시글의 태그 목록 조회",
        description = """
                    특정 게시글에 등록된 태그 목록을 반환합니다.
                    (단순 Post → Tag 매핑 조회)

                    반환 예:
                    [
                      { "tagId": 1, "name": "spring" },
                      { "tagId": 2, "name": "cloud" }
                    ]
                    """
    )
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagResponse>> getPostTags(@PathVariable("id") Long id) {

        List<TagResponse> tags = postTagService.getByPostId(id)
            .stream()
            .map(tagDTO ->
                new TagResponse(tagDTO.getPostTag().getTag().getId(), tagDTO.getName())
            )
            .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    // ============================================================
    // 📌 5. 게시글 신규 등록
    // ============================================================
    @Operation(
        summary = "게시글 생성",
        description = """
                    새로운 게시글을 생성합니다.

                    ✔ 인증 필요 (JWT)
                    ✔ 작성자는 Authentication 기반으로 자동 식별
                    ✔ 저장 후 생성된 postId만 반환

                    호출 흐름:
                    Post-Frontend
                      → POST /api/posts
                      → PostService.createPost()
                    """
    )
    @PostMapping
    public ResponseEntity<?> createPost(
        Authentication authentication,
        @Valid @RequestBody  PostRequestDTO dto
    ) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();

        Post saved = postService.createPost(dto, memberUuid);

        return ResponseEntity.ok(saved.getId()); // 일단 ID만 반환
    }

    // ============================================================
    // 📌 6. 게시글 수정
    // ============================================================
    @Operation(
        summary = "게시글 수정",
        description = """
                    기존 게시글을 수정합니다.

                    ✔ 인증 필요 (JWT)
                    ✔ 작성자 본인만 수정 가능
                    ✔ 작성자 UUID(dto.authorUuid)와 인증된 사용자의 UUID 비교

                    반환값:
                    - 수정된 게시글의 ID
                    """
    )
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(
        Authentication authentication,
        @RequestBody PostUpdateDTO dto) {

        // 1. 게시글 존재 여부 확인
        Post post = postService.getPostById(dto.id())
            .orElseThrow(() -> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        // 2. 권한 체크 (본인만 수정 가능)
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();

        if(!memberUuid.equals(dto.authorUuid())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 4. 업데이트 수행
        Post updated = postService.editPost(dto);

        // 5. postId 반환
        return ResponseEntity.ok(updated.getId());
    }

    // ============================================================
    // 📌 7. 게시글 삭제
    // ============================================================
    @Operation(
        summary = "게시글 삭제",
        description = """
                    특정 게시글을 삭제합니다.

                    ✔ 인증 필요
                    ✔ 작성자 본인만 삭제 가능
                    ✔ 삭제 시 댓글/첨부 등 관련 데이터는 PostDeleteFacadeService에서 일괄 처리

                    응답: HTTP 204 No Content
                    """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(Authentication authentication, @PathVariable("id") Long id) {

        // 1. 게시글 존재 여부 확인
        Post post = postService.getPostById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        // 2. 권한 체크 (본인만 수정 가능)
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();

        if(!memberUuid.equals(post.getMemberUuid())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        postDeleteFacadeService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 반환
    }
}


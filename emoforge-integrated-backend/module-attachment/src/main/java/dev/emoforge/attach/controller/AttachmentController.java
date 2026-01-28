package dev.emoforge.attach.controller;

import dev.emoforge.attach.domain.Attachment;
import dev.emoforge.attach.domain.AttachmentStatus;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.dto.AttachmentConfirmRequest;
import dev.emoforge.attach.dto.AttachmentDeleteRequest;
import dev.emoforge.attach.dto.AttachmentMapper;
import dev.emoforge.attach.dto.AttachmentResponse;
import dev.emoforge.attach.service.AttachmentService;
import dev.emoforge.attach.util.FileDownloadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 첨부파일 업로드/조회/삭제 및 게시글·프로필 이미지 관리를 담당하는 Controller.
 *
 * - 에디터 이미지, 일반 첨부파일, 프로필 이미지 업로드
 * - 게시글별 첨부 개수/목록 조회 (Post-Service BFF 용도 포함)
 * - 첨부파일 확정/일괄 삭제, 단일 삭제, 다운로드 등
 *
 * 일부 엔드포인트는 프론트엔드에서 직접 호출되지 않고,
 * Post-Service / Auth-Service에서 Feign/BFF 형태로 내부 호출된다.
 */
@Tag(
        name = "Attachment API",
        description = "파일 업로드/다운로드 및 게시글·프로필 첨부 관리 API"
)
@RestController
@RequestMapping("/api/attach")
@RequiredArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final FileDownloadUtil fileDownloadUtil;

    @Operation(
            summary = "파일 업로드",
            description = """
                    파일(프로필 이미지, 에디터 이미지, 일반 첨부파일)을 업로드합니다.
                    
                    ✔ uploadType
                    - PROFILE_IMAGE: 프로필 이미지 업로드
                    - EDITOR_IMAGE: 게시글 에디터 내 이미지 업로드
                    - ATTACHMENT: 게시글 첨부파일 업로드
                    
                    ✔ attachmentStatus
                    - TEMP: 게시글 등록 전 임시 상태 (tempKey로 그룹화)
                    - CONFIRMED: 게시글 등록 성공 후 확정 상태
                    
                    ✔ 파라미터 사용 규칙
                    - postId: ATTACHMENT / EDITOR_IMAGE 등 게시글과 연결되는 업로드에 사용
                    - memberUuid: PROFILE_IMAGE 업로드 시 업로더(사용자)를 식별하는 데 사용
                    - tempKey: 게시글 작성 중 임시 첨부들을 그룹핑할 때 사용
                    """
    )
    @PostMapping
    public ResponseEntity<AttachmentResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadType") UploadType uploadType,
            @RequestParam("attachmentStatus") AttachmentStatus attachmentStatus,
            @RequestParam(value = "postId", required = false) Long postId,
            @RequestParam(value = "memberUuid", required = false) String memberUuid,
            @RequestParam(value = "tempKey", required = false) String tempKey
    ) throws IOException {
        Attachment saved = attachmentService.uploadFile(file, uploadType, attachmentStatus, postId, memberUuid, tempKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AttachmentMapper.toResponse(saved));
    }

    /**
     * 파일 삭제 (soft delete + 실제 파일 삭제는 아직 미구현)
     */
    @Operation(
            summary = "단일 첨부파일 삭제",
            description = """
                    첨부파일 ID로 단일 첨부를 삭제합니다.
                    현재는 soft delete 기준으로 동작하며,
                    실제 파일 삭제는 별도 클린업 로직에서 처리될 수 있습니다.
                    """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable("id") Long id) {
        attachmentService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * 사용자 프로필 이미지 조회 (최신 1개)
     */
    @Operation(
            summary = "사용자 프로필 이미지 조회 (최신 1개)",
            description = """
                    특정 사용자의 최신 프로필 이미지 정보를 조회합니다.
                    존재하지 않을 경우 404(Not Found)를 반환합니다.
                    """
    )
    @GetMapping("/profile/{memberUuid}")
    public ResponseEntity<AttachmentResponse> getProfileImage(@PathVariable("memberUuid") String memberUuid) {
        return attachmentService.getProfileImage(memberUuid)
                .map(AttachmentMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 특정 게시글이 첨부 업로드 가능한 상태인지 확인 (3개 제한)
     */
    @Operation(
            summary = "게시글 첨부 업로드 가능 여부 확인",
            description = """
                    특정 게시글(postId)에 대해 첨부파일을 추가로 업로드할 수 있는지 여부를 확인합니다.
                    기본 정책: 게시글당 최대 3개의 첨부파일 제한.
                    
                    반환값:
                    - true: 추가 업로드 가능
                    - false: 이미 최대 개수에 도달하여 업로드 불가
                    """
    )
    @GetMapping("/post/{postId}/can-upload")
    public ResponseEntity<Boolean> canUploadAttachment(@PathVariable Long postId) {
        return ResponseEntity.ok(attachmentService.canUploadAttachment(postId));
    }

    /**
     * post에 첨부된 파일 개수 구하기(Post-Service 에서 bbf로직에서 사용)
     */
    @Operation(
            summary = "게시글별 첨부파일 개수 일괄 조회",
            description = """
                    여러 게시글 ID에 대해 첨부파일 개수를 일괄 조회합니다.
                    
                    ✔ 사용처
                    - Post-Service BFF 로직에서 게시글 목록과 함께 첨부 개수를 표시할 때 사용합니다.
                    
                    요청: [postId1, postId2, ...]
                    응답: { postId1: count1, postId2: count2, ... }
                    """
    )
    @PostMapping("/count/batch")
    public Map<Long, Integer> countByPostIds(@RequestBody List<Long> postIds) {

        return attachmentService.countByPostIds(postIds);
    }

    /**
     * post에 첨부된 파일 정보 구하기(Post-Service 에서 bbf로직에서 사용)
     * uploadType (EDITOR_IMAGE, ATTACHMENT)
     */
    @Operation(
            summary = "게시글 첨부파일 목록 조회",
            description = """
                    특정 게시글(postId)에 연결된 첨부파일 목록을 조회합니다.
                    
                    ✔ uploadType
                    - EDITOR_IMAGE: 에디터 본문 이미지
                    - ATTACHMENT: 일반 첨부파일
                    
                    ✔ 사용처
                    - Post-Service에서 게시글 상세보기/수정 화면 등에 BFF 방식으로 사용합니다.
                    """
    )
    @GetMapping("/post/{postId}")
    public List<AttachmentResponse> findByPostId(@PathVariable("postId") Long postId, @RequestParam("uploadType") UploadType uploadType) {
        return attachmentService.findByPostId(postId, uploadType);
    }

    @Operation(
            summary = "사용자 최신 프로필 이미지 조회 (null 허용)",
            description = """
                    특정 사용자의 최신 프로필 이미지를 조회합니다.
                    프로필 이미지가 없는 경우, 응답 본문에 null을 반환할 수 있습니다.
                    
                    (내부적으로 getProfileImage와 동일한 로직을 사용하지만,
                    응답 형태가 다를 수 있어 별도로 사용될 수 있습니다.)
                    """
    )
    @GetMapping("/profile-images/{memberUuid}")
    public ResponseEntity<AttachmentResponse> getLatestProfileImage(@PathVariable("memberUuid") String memberUuid) {
        return ResponseEntity.ok(
                attachmentService.getProfileImage(memberUuid)
                        .map(AttachmentMapper::toResponse)
                        .orElse(null)   // 없는 경우 null 반환
        );
    }

    /**
     * Post 등록이 성공하면 postId에 가져온 postId값과 status를 CONFIRMED로 업데이트 한다.
     */
    @Operation(
            summary = "임시 첨부파일 확정 처리",
            description = """
                    게시글 등록이 성공한 이후, 임시 상태(TEMP)의 첨부파일들을
                    실제 게시글(postId) 기준으로 확정(CONFIRMED) 처리합니다.
                    
                    ✔ 사용 흐름 예시
                    1) 게시글 작성 중 tempKey 기준으로 첨부 업로드 (TEMP 상태)
                    2) 게시글 등록 성공 후, 발급된 postId + groupTempKey 로 본 API 호출
                    3) 해당 tempKey에 묶여 있던 첨부파일들을 postId에 귀속시키고 상태를 CONFIRMED로 변경
                    """
    )
    @PostMapping("/confirm")
    public ResponseEntity<?> attachmentConfirm(@RequestBody AttachmentConfirmRequest request) {

        log.debug("\n\n====confirm");
        log.debug("postId : " + request.postId());
        log.debug("groupTempKey : " + request.groupTempKey());

        attachmentService.confirmAttachments(request.postId(), request.groupTempKey());
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 첨부파일 일괄 삭제 처리
     *
     * @param request 첨부파일 ID 목록
     * @return 성공 여부 응답
     */
    @Operation(
            summary = "첨부파일 ID 목록 기반 일괄 삭제",
            description = """
                    여러 첨부파일 ID를 한 번에 삭제합니다.
                    주로 게시글 수정/삭제 시, 필요 없는 첨부들을 일괄 정리하는 용도로 사용됩니다.
                    """
    )
    @PostMapping("/delete/batch")
    public ResponseEntity<?> deleteBatch(@RequestBody AttachmentDeleteRequest request) {
        attachmentService.deleteBatch(request.getAttachmentIds());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "첨부파일 다운로드",
            description = """
                    첨부파일 ID로 파일을 다운로드합니다.
                    Content-Disposition 헤더를 통해 브라우저에서 다운로드가 일어나도록 응답을 구성합니다.
                    """
    )
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        Attachment attachment = attachmentService.getById(id).orElseThrow(() -> new IllegalArgumentException("해당 id를 갖는 첨부파일이 없습니다!"));
        return fileDownloadUtil.getDownloadResponse(attachment.getFileUrl(), attachment.getOriginFileName());
    }

    /**
     * 특정 게시글 아이디(postId)의 첨부파일 정보 삭제
     */
    @Operation(
            summary = "게시글 단위 첨부파일 일괄 삭제",
            description = """
                    특정 게시글 ID(postId)에 연결된 모든 첨부파일 정보를 삭제합니다.
                    게시글 삭제 시, 연관 첨부를 함께 정리하는 용도로 사용됩니다.
                    """
    )
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deleteByPostId(@PathVariable("postId") Long postId) {
        attachmentService.deleteByPostId(postId);
        return ResponseEntity.noContent().build(); // ✅ 204 No Content
    }
}

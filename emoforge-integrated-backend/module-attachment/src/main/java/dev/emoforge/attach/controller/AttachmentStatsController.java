package dev.emoforge.attach.controller;

import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.dto.MemberAttachmentStatsResponse;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.attach.service.AttachmentStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 사용자가 업로드한 첨부파일의 유형별 개수를 조회하는 컨트롤러.
 *
 * 이 API는 로그인한 사용자가 업로드한 파일들을 유형(EDITOR_IMAGE, ATTACHMENT)별로
 * 몇 개씩 등록했는지 통계 형태로 반환합니다.
 *
 * ✔ Authentication 기반 사용자 식별
 * ✔ editor 이미지, 일반 첨부파일을 각각 개수로 반환
 * ✔ 마이페이지·프로필 대시보드 등에서 사용
 *
 * 주요 활용 예:
 * - 사용자 프로필 → "내 첨부파일 요약"
 * - 파일 용량 관리 UI
 * - 에디터 이미지와 일반 첨부파일 구분 통계
 */
@Tag(
        name = "Attachment Statistics API",
        description = "사용자가 업로드한 첨부파일을 유형별로 집계하여 제공하는 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attach")
@Slf4j
public class AttachmentStatsController {

    private final AttachmentStatsService attachmentStatsService;

    @Operation(
            summary = "내 첨부파일 유형별 통계 조회",
            description = """
                    로그인한 사용자가 업로드한 첨부파일을 유형별(EDITOR_IMAGE, ATTACHMENT)로 집계하여 반환합니다.
                    
                    ✔ Authentication → memberUuid 식별
                    ✔ 업로드 타입별 개수(Map) → DTO 변환  
                    ✔ 프로필, 마이페이지 요약 대시보드에서 사용
                    
                    반환 예:
                    {
                      "editorImageCount": 10,
                      "attachmentCount": 3
                    }
                    """
    )
    @GetMapping("/me/statistics")
    public ResponseEntity<MemberAttachmentStatsResponse> getMyAttachmentStats(
            Authentication authentication
    ) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        log.info("📊 Attachment statistics 요청: memberUuid={}", memberUuid);

        Map<UploadType, Long> stats = attachmentStatsService.getUserAttachmentCounts(memberUuid);

        // Map → DTO 변환
        MemberAttachmentStatsResponse response = new MemberAttachmentStatsResponse(
                stats.getOrDefault(UploadType.EDITOR_IMAGE, 0L),
                stats.getOrDefault(UploadType.ATTACHMENT, 0L)
        );

        return ResponseEntity.ok(response);
    }
}

package dev.emoforge.attach.controller;

import dev.emoforge.attach.service.AttachmentCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attach/cleanup")
@RequiredArgsConstructor
@Slf4j
public class AttachmentCleanupController {

    private final AttachmentCleanupService attachmentCleanupService;

    @Operation(
            summary = "에디터 임시 이미지 정리",
            description = """
                    ToastUI 마크다운 에디터에서 **이미지를 첨부했다가 삭제한 경우**,  
                    에디터에는 남아있지 않지만 서버에는 남아있는 **임시 이미지 파일(DB + 디스크)** 를 정리합니다.

                    ✔ 사용 시점  
                    - 게시글 작성/수정 완료 시  
                    - 최종적으로 에디터에 남아있는 이미지 목록(fileUrls)을 바탕으로  
                      **불필요한 이미지 파일을 삭제**합니다.
                    """
    )
    @PostMapping("/editor")
    public ResponseEntity<Void> cleanupEditor(@RequestBody CleanupRequest request) {

        attachmentCleanupService.cleanupEditorImages(request.getPostId(), request.getFileUrls());
        return ResponseEntity.ok().build();
    }

    @Schema(description = "에디터 이미지 정리 요청 DTO")
    @Data
    @ToString
    public static class CleanupRequest {

        @Schema(
                description = "게시글 ID (Post ID)",
                example = "42"
        )
        private Long postId;

        @Schema(
                description = """
                        에디터에 **최종적으로 남아있는 이미지 URL 목록**  
                        서버는 이 목록에 포함되지 않은 이미지들을 '사용되지 않은 이미지'라고 판별하여 삭제합니다.
                        """,
                example = """
                        [
                          "https://www.emoforge.dev/uploads/editor_images/tmp_123abc.png",
                          "https://www.emoforge.dev/uploads/editor_images/tmp_456def.png"
                        ]
                        """
        )
        private List<String> fileUrls; // 에디터에 남아있는 이미지 URL
    }
}

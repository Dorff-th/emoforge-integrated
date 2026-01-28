package dev.emoforge.attach.dto;

import dev.emoforge.attach.domain.UploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 파일 업로드/조회 시 반환되는 응답 DTO.
 *
 * 업로드된 파일의 식별자, 이름, URL, 타입 등
 * 프론트엔드가 화면에 표시하거나 후속 처리에 사용할 정보를 포함한다.
 *
 * 사용 API:
 * - POST /api/attach              (파일 업로드 결과)
 * - GET  /api/attach/profile/{memberUuid}
 * - GET  /api/attach/post/{postId}
 * - GET  /api/attach/profile-images/{memberUuid}
 */
@Schema(description = "첨부파일 응답 DTO")
@Data
@Builder
public class AttachmentResponse {

    @Schema(description = "첨부파일 ID", example = "101")
    private Long id;

    @Schema(description = "게시글 ID (프로필 이미지일 경우 null)", example = "42")
    private Long postId;

    @Schema(description = "업로드한 사용자 UUID", example = "c1a2b3d4-e5f6-7890-abcd-123456789000")
    private String memberUuid;

    @Schema(description = "원본 파일명", example = "my_photo.png")
    private String originFileName;

    @Schema(description = "서버에 저장된 파일명", example = "450577c636474c63a81929d230616e5a.png")
    private String fileName;

    @Schema(description = "파일 타입(MIME Type)", example = "image/png")
    private String fileType;

    @Schema(description = "파일 크기(bytes)", example = "245123")
    private long fileSize;

    @Schema(
            description = "공개 URL, 프론트에서 바로 접근 가능한 URL",
            example = "https://www.emoforge.dev/api/attach/uploads/profile_image/0a7fbfd15fa040e5a01f4e979eec002f.png"
    )
    private String publicUrl;

    @Schema(
            description = "업로드 타입",
            example = "PROFILE_IMAGE",
            allowableValues = { "PROFILE_IMAGE", "EDITOR_IMAGE", "ATTACHMENT" }
    )
    private UploadType uploadType;

    @Schema(
            description = "업로드된 시각",
            example = "2025-02-01T13:25:42"
    )
    private LocalDateTime uploadedAt;

    @Schema(
            description = "파일 크기의 텍스트 버전 (UI 표시용)",
            example = "2.3 MB"
    )
    private String fileSizeText;  // 화면용: "2.3 MB"
}

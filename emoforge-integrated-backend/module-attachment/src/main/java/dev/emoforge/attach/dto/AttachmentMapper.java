package dev.emoforge.attach.dto;

import dev.emoforge.attach.domain.Attachment;
/**
 * Attachment 엔티티 → AttachmentResponse DTO 변환 전용 유틸 클래스.
 *
 * - Controller/Service 계층에서 엔티티를 직접 노출하지 않기 위해 사용됨.
 * - 변환 규칙을 한 곳에 모아두어 재사용성과 유지보수성을 높임.
 * - Swagger 문서화 대상은 아니며, DTO 변환 책임만 가짐.
 */
public class AttachmentMapper {

    public static AttachmentResponse toResponse(Attachment entity) {
        return AttachmentResponse.builder()
                .id(entity.getId())
                .postId(entity.getPostId())
                .memberUuid(entity.getMemberUuid())
                .originFileName(entity.getOriginFileName())
                .fileName(entity.getFileName())
                .fileType(entity.getFileType())
                .fileSize(entity.getFileSize())
                .publicUrl(entity.getPublicUrl())
                .uploadType(entity.getUploadType())
                .uploadedAt(entity.getUploadedAt())
                .build();
    }
}

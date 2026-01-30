package dev.emoforge.post.dto.legacy.external;


import dev.emoforge.attach.domain.Attachment;
import dev.emoforge.attach.util.FormatFileSize;
import lombok.Builder;

/**
 * 업로드 결과 응답 DTO
 */
@Builder
public record AttachmentResponse (
     Long id,
     Long postId,
     String memberUuid,
     String originFileName,
     String fileName,
     String fileType,
     long fileSize,
     String publicUrl,
    String fileSizeText
) {
    public static AttachmentResponse from(Attachment attachment) {
        return new AttachmentResponse(
                attachment.getId(),
                attachment.getPostId(),
                attachment.getMemberUuid(),
                attachment.getOriginFileName(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getPublicUrl(),
                FormatFileSize.formatFileSize(attachment.getFileSize())
        );
    }
}

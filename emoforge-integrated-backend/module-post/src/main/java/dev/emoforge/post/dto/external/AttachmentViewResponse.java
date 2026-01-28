package dev.emoforge.post.dto.external;

public record AttachmentViewResponse(
        Long id,
        String filename,
        String url
) {
}

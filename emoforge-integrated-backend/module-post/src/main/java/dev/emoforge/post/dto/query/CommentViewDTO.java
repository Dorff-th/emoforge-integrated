package dev.emoforge.post.dto.query;

import dev.emoforge.post.dto.internal.CommentResponse;

import java.time.LocalDateTime;

public record CommentViewDTO(
        Long id,
        Long postId,
        String memberUuid,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String profileImageUrl
) {
    public static CommentViewDTO from(CommentViewProjection p) {
        return new CommentViewDTO(
                p.getId(),
                p.getPostId(),
                p.getMemberUuid(),
                p.getNickname(),
                p.getContent(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getProfileImageUrl()
        );
    }
}


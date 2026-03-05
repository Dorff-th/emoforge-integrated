package dev.emoforge.post.admin.dto;

import java.time.LocalDateTime;

public record AdminPostListItemDTO(
    Long id,
    String title,
    int viewCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String memberUuid,
    Long categoryId,
    LocalDateTime adminModifiedAt,
    String adminModifiedBy
) {
}

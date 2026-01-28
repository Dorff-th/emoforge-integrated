package dev.emoforge.post.dto.internal;

import java.time.LocalDateTime;

/**
 * @description Post목록 기본조회(internal)
 *
 * @param id
 * @param title
 * @param createdAt
 * @param categoryName
 * @param memberUuid
 * @param commentCount
 */
public record PostSimpleDTO(
    Long id,
    String title,
    LocalDateTime createdAt,
    String categoryName,
    String memberUuid,
    Long commentCount
) {}

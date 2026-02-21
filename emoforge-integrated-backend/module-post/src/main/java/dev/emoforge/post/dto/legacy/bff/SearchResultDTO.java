package dev.emoforge.post.dto.legacy.bff;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;

import java.time.LocalDateTime;

@Hidden
@Builder
public record SearchResultDTO(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        String authorNickname,
        Long categoryId,
        String categoryName,
        String commentContent,
        Long commentCount

        /*String commentWriter,
        String summary,
        MatchedField matchedField,
        String highlightedTitle*/
) {
}

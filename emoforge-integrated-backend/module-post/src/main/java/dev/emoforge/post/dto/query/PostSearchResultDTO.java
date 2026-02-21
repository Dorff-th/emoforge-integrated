package dev.emoforge.post.dto.query;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PostSearchResultDTO {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private String memberUuid;
        private String authorNickname;
        private Long categoryId;
        private String categoryName;
        private String commentContent;
        private Long commentCount;

}

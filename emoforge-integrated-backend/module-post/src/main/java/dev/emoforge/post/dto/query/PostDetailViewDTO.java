package dev.emoforge.post.dto.query;

import dev.emoforge.post.dto.legacy.external.AttachmentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailViewDTO {

    private Long id;
    private String title;
    private String content;

    private String memberUuid;
    private String nickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long categoryId;
    private String categoryName;

    //private List<AttachmentResponse> attachments;
}


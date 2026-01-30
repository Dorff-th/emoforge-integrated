package dev.emoforge.post.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListItemSummary {

    private Long postId;

    private String title;

    private LocalDateTime createdAt;

    private String categoryName;

    private String nickname;

    private Long commentCount;

    private Integer attachmentCount;

    public static PostListItemSummary from(PostListItemProjection p) {
        return PostListItemSummary.builder()
                .postId(p.getPostId())
                .title(p.getTitle())
                .createdAt(p.getCreatedAt())
                .categoryName(p.getCategoryName())
                .nickname(p.getNickname())
                .commentCount(p.getCommentCount())
                .attachmentCount(p.getAttachmentCount())
                .build();
    }
}

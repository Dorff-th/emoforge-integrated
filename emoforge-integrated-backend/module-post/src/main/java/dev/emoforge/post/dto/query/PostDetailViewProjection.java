package dev.emoforge.post.dto.query;

import java.time.LocalDateTime;

public interface PostDetailViewProjection {

    Long getId();
    String getMemberUuid();
    String getNickname();
    String getTitle();
    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Long getCategoryId();
    String getCategoryName();
}

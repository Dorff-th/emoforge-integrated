package dev.emoforge.post.dto.query;

import java.time.LocalDateTime;

public interface CommentViewProjection {

    Long getId();
    Long getPostId();

    String getMemberUuid();
    String getNickname();

    String getContent();

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    String getProfileImageUrl();
}


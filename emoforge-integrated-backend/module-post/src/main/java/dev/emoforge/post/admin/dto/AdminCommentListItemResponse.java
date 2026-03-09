package dev.emoforge.post.admin.dto;

import java.time.LocalDateTime;

public interface AdminCommentListItemResponse {

    Long getCommentId();

    String getContent();

    String getAuthor();

    Long getPostId();

    String getPostTitle();

    LocalDateTime getCreatedAt();
}
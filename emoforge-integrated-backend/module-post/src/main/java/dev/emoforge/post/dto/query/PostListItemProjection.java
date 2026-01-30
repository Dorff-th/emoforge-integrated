package dev.emoforge.post.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


public interface PostListItemProjection {

        Long getPostId();

        String getTitle();

        LocalDateTime getCreatedAt();

        String getCategoryName();

        String getNickname();

        Long getCommentCount();

        int getAttachmentCount();
}

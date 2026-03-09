package dev.emoforge.post.admin.dto;

import java.util.List;

public record AdminCommentBulkDeleteRequest(
    List<Long> commentIds
) {
}

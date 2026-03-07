package dev.emoforge.post.admin.dto;

import java.util.List;

public record AdminPostBulkDeleteRequest(
    List<Long> postIds
) {
}

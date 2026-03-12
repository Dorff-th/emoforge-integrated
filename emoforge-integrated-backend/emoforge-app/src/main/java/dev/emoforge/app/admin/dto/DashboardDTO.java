package dev.emoforge.app.admin.dto;

public record DashboardDTO(
        long totalMembers,
        long totalPosts,
        long totalComments,
        long profileImageAttachments,
        long editorImageAttachments,
        long generalAttachments
) {
}

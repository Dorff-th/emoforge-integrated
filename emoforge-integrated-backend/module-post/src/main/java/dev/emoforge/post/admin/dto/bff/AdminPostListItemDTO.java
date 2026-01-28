package dev.emoforge.post.admin.dto.bff;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "관리자 게시글 목록 아이템")
public record AdminPostListItemDTO(

    @Schema(description = "게시글 ID", example = "42")
    Long id,

    @Schema(description = "게시글 제목", example = "Spring Boot로 JWT 인증 구현하기")
    String title,

    @Schema(description = "작성일시", example = "2025-11-18T09:10:22")
    LocalDateTime createdAt,

    @Schema(description = "카테고리 이름", example = "Spring")
    String categoryName,

    // ---------- Auth-Service BFF 영역 ----------
    @Schema(description = "작성자 닉네임", example = "행복한호랑이")
    String nickname,

    // ---------- Comment / Attach BFF 영역 ----------
    @Schema(description = "댓글 개수", example = "12")
    Long commentCount,

    @Schema(description = "첨부파일 개수", example = "3")
    int attachmentCount
) {
}

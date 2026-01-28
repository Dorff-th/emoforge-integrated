package dev.emoforge.post.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 사용자가 작성한 게시글·댓글 개수를 담는 간단 통계 응답 DTO.
 *
 * ✔ postCount: 작성한 게시글 수
 * ✔ commentCount: 작성한 댓글 수
 *
 * 마이페이지·프로필 대시보드 등에서 활동량 요약용으로 사용됩니다.
 */
@Schema(description = "사용자 게시글·댓글 통계 응답 DTO")
@Builder
public record PostStatsResponse(

    @Schema(description = "사용자가 작성한 게시글 수", example = "12")
    long postCount,

    @Schema(description = "사용자가 작성한 댓글 수", example = "34")
    long commentCount
) {}

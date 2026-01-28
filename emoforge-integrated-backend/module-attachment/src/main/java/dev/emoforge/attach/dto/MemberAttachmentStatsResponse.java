package dev.emoforge.attach.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자가 업로드한 첨부파일을 유형별로 집계한 통계 응답 DTO.
 *
 * ✔ editorImageCount: 에디터에서 업로드한 이미지 개수
 * ✔ attachmentCount: 일반 첨부파일 개수
 *
 * 마이페이지·프로필 통계 화면에서 사용됩니다.
 */
@Schema(description = "사용자의 첨부파일을 유형별로 집계한 통계 응답 DTO")
public record MemberAttachmentStatsResponse(
        @Schema(description = "에디터에서 업로드한 이미지 개수", example = "10")
        long editorImageCount,
        @Schema(description = "일반 첨부파일 개수", example = "3")
        long attachmentCount
) {}
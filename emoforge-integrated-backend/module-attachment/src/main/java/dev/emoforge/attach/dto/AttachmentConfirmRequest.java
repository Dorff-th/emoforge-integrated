package dev.emoforge.attach.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 첨부파일 확정 요청 DTO.
 *
 * 게시글 작성 완료 후, TEMP 상태였던 첨부파일들을
 * 실제 postId와 매핑시키고 상태를 CONFIRMED로 변경하기 위한 요청 데이터.
 *
 * 사용 API:
 * - POST /api/attach/confirm
 */
@Schema(description = "첨부파일 확정 요청 DTO")
public record AttachmentConfirmRequest(

        @Schema(
                description = "게시글 작성 중 업로드된 임시 첨부파일들을 그룹핑하는 tempKey",
                example = "temp_123abc_group"
        )
        String groupTempKey,

        @Schema(
                description = "등록된 게시글의 ID",
                example = "42"
        )
        Long postId
) {}

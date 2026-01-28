package dev.emoforge.attach.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 첨부파일 일괄 삭제 요청 DTO.
 *
 * 여러 첨부파일을 한 번에 삭제할 때 사용된다.
 * 주로 게시글 수정/삭제 시, 불필요한 첨부파일들을 정리하기 위한 용도.
 *
 * 사용 API:
 * - POST /api/attach/delete/batch
 */
@Schema(description = "첨부파일 일괄 삭제 요청 DTO")
@Data
public class AttachmentDeleteRequest {

    @Schema(
            description = "삭제할 첨부파일 ID 목록",
            example = "[12, 15, 19]"
    )
    private List<Long> attachmentIds; // 삭제할 첨부파일 ID 목록
}

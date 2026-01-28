package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * GPTSummaryResponseDTO
 *
 * GPT가 생성한 회고 요약을 반환하는 응답 DTO.
 *
 * 사용 위치:
 *  - DiaryEntryController.generateSummary()
 *      특정 날짜의 회고 내용을 기반으로 GPT가 생성한 요약을 반환.
 *
 *  - SummaryController.getTodayGptSummary()
 *      오늘 날짜 기준으로 저장된 GPT 회고 요약을 조회.
 *
 * 제공 필드:
 *  ✔ summary : GPT가 생성한 하루 회고 요약 문장
 */
@Schema(description = "GPT 회고 요약 응답 DTO")
@Data
@AllArgsConstructor
public class GPTSummaryResponseDTO {
    @Schema(
            description = "GPT가 생성한 하루 요약 텍스트",
            example = "오늘은 비교적 차분한 하루였으며, 기분 변화가 적었습니다."
    )
    private String summary;
}

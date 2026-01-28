package dev.emoforge.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * GPTSummaryRequestDTO
 *
 * GPT 회고 요약 생성 요청 DTO.
 *
 * 사용 위치:
 *  - DiaryEntryController.generateSummary()
 *
 * 특정 날짜(date)를 전달하면
 * 해당 날짜에 작성된 회고 내용을 기반으로 GPT가 요약을 생성한다.
 *
 * 제공 필드:
 *  ✔ date : 요약을 생성할 대상 날짜 (YYYY-MM-DD)
 */
@Schema(description = "GPT 회고 요약 생성 요청 DTO")
@Data
public class GPTSummaryRequestDTO {

    @Schema(
            description = "GPT 요약을 생성할 날짜 (YYYY-MM-DD)",
            example = "2025-01-12"
    )
    private LocalDate date;
}

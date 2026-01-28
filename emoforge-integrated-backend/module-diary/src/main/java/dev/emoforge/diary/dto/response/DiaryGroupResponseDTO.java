package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DiaryGroupResponseDTO
 *
 * 월간 회고 달력 조회(/api/diary/diaries/monthly) API에서 사용되는
 * "날짜별 회고 그룹" 단위 응답 DTO.
 *
 * ✔ date: 해당 날짜 (YYYY-MM-DD)
 * ✔ summary: GPT 회고 요약 (nullable, 요약이 생성되지 않았을 경우 null)
 * ✔ entries: 해당 날짜에 작성된 회고 리스트
 *
 * DiaryEntryController.getDiaryListMonthly() 에서 사용되며,
 * React '달력 화면'에서 특정 날짜에 회고가 있는지, 요약이 생성되었는지 등을
 * 표시하는 데 활용된다.
 */
@Schema(description = "날짜별 회고 그룹 응답 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryGroupResponseDTO {

    @Schema(
            description = "해당 날짜 (YYYY-MM-DD)",
            example = "2025-01-05"
    )
    private LocalDate date;          // 날짜

    @Schema(
            description = "GPT 생성 하루 요약 (없을 경우 null)",
            example = "오늘은 비교적 차분한 하루였고, 감정 기복이 적었습니다."
    )
    private String summary;          // GPT 하루 요약 (nullable)

    @Schema(
            description = "해당 날짜에 작성된 회고 리스트"
    )
    private List<DiaryEntryDTO> entries;  // 해당 날짜의 회고들
}

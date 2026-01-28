package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * DiarySearchResultDTO
 *
 * 회고 통합검색(search) 결과의 단일 엔트리를 나타내는 응답 DTO.
 *
 * 사용 위치:
 *  - DiarySearchController.search()
 *
 * 검색 조건(query, fields, emotionMap, diaryDateMap 등)에 따라 조회된
 * 회고(DiaryEntry) 한 건의 핵심 정보를 포함한다.
 *
 * 제공 필드:
 *  ✔ id          : 회고 ID
 *  ✔ diaryDate   : 회고 날짜 (YYYY-MM-DD)
 *  ✔ content     : 회고 본문
 *  ✔ feelingKo   : 한글 감정 문장
 *  ✔ feelingEn   : GPT 영어 감정 문장
 *  ✔ feedback    : GPT 피드백
 *  ✔ emotion     : 감정 점수 (1~5)
 *  ✔ createdAt   : 작성 시각
 */
@Schema(description = "회고 통합검색 결과 DTO")
@Getter
@Builder
public class DiarySearchResultDTO {

    @Schema(description = "회고 고유 ID", example = "120")
    private Long id;

    @Schema(description = "회고 날짜 (YYYY-MM-DD)", example = "2025-01-12")
    private LocalDate diaryDate;

    @Schema(
            description = "회고 본문 내용",
            example = "운동하고 책 읽으며 차분하게 하루를 보냈다."
    )
    private String content;

    @Schema(
            description = "한글 감정 문장",
            example = "오늘은 침착하고 안정적인 감정 상태였다."
    )
    private String feelingKo;

    @Schema(
            description = "GPT가 생성한 영어 감정 문장",
            example = "It was a calm and steady day."
    )
    private String feelingEn;

    @Schema(
            description = "GPT 피드백 문장",
            example = "지금처럼 차분한 루틴을 유지해보는 것도 좋아요!"
    )
    private String feedback;

    @Schema(
            description = "감정 점수 (1~5)",
            example = "4"
    )
    private int emotion;

    @Schema(
            description = "작성 시각 (LocalDateTime)",
            example = "2025-01-12T09:32:11"
    )
    private LocalDateTime createdAt;
}

package dev.emoforge.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DiarySaveRequestDTO
 *
 * 회고 작성 요청 DTO.
 * DiaryEntryController.saveDiary() 에서 사용되며,
 * 하루의 감정 점수, 습관 체크, 감정 문장, 본문, GPT 피드백 등을
 * 하나의 요청으로 전달한다.
 *
 * 제공 필드:
 *  ✔ diaryDate     : 회고 날짜 (YYYY-MM-DD)
 *  ✔ emotionScore  : 감정 점수 (1~5)
 *  ✔ habitTags     : 완료한 습관 태그 목록
 *  ✔ feelingKo     : 오늘의 감정 한글 문장
 *  ✔ feelingEn     : GPT 생성 영어 문장
 *  ✔ content       : 회고 본문
 *  ✔ feedback      : GPT 생성 피드백 문장
 */
@Schema(description = "회고 저장 요청 DTO")
@Getter
@Setter
public class DiarySaveRequestDTO {

    @Schema(
            description = "회고 날짜 (YYYY-MM-DD)",
            example = "2025-01-10"
    )
    private LocalDate diaryDate;         // 회고 날짜

    @Schema(
            description = "감정 점수 (1~5 범위)",
            example = "4"
    )
    private int emotionScore;            // 감정 점수 (1~5)

    @Schema(
            description = "오늘 완료한 습관 태그 리스트",
            example = "[\"운동\", \"독서\", \"명상\"]"
    )
    private List<String> habitTags;      // 오늘 완료한 습관 리스트

    @Schema(
            description = "한글 감정 문장",
            example = "오늘은 전체적으로 편안하고 차분한 하루였다."
    )
    private String feelingKo;            // 한글 감정 문장

    @Schema(
            description = "GPT가 생성한 영어 감정 문장",
            example = "It was a calm and peaceful day overall."
    )
    private String feelingEn;            // GPT 영어 문장

    @Schema(
            description = "회고 본문 내용",
            example = "아침에 운동을 하고 책을 읽으면서 하루를 차분하게 보냈다."
    )
    private String content;              // 회고 본문

    @Schema(
            description = "GPT가 제공한 피드백 문장",
            example = "오늘의 루틴은 아주 좋아요! 이런 일정이 일관되게 유지된다면 긍정적인 감정 흐름을 만들 수 있어요."
    )
    private String feedback;             // GPT가 생성한 피드백
}


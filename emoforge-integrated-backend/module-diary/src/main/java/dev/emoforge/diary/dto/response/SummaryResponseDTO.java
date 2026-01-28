package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
/**
 * SummaryResponseDTO
 *
 * 홈 화면 요약(today summary)에서 사용하는 핵심 응답 DTO.
 *
 * SummaryController.getTodaySummary() 에서 반환되며,
 * 오늘 하루의 감정 상태 + 회고 본문 + 습관 + GPT 요약/피드백 정보를 한 번에 제공한다.
 *
 * 구성 정보:
 *  ✔ diaryDate     : 오늘 날짜 (기록 기준)
 *  ✔ emotion       : 감정 점수 (1~5)
 *  ✔ feelingKo     : 한글 감정 문장
 *  ✔ feelingEn     : GPT가 생성한 영어 감정 문장
 *  ✔ habitTags     : 실천한 습관 태그(예: "운동,독서,명상")
 *  ✔ content       : 사용자가 작성한 오늘의 회고 본문
 *  ✔ summary       : GPT 회고 요약
 *  ✔ feedback      : GPT 피드백 문장
 *
 * 이 DTO가 프론트의 "오늘의 감정 요약" 화면을 구성하는 전체 데이터 구조다.
 */
@Schema(description = "홈 화면 '오늘 하루 요약' 응답 DTO")
public record SummaryResponseDTO(

        @Schema(
                description = "회고 날짜 (YYYY-MM-DD)",
                example = "2025-01-12"
        )
        LocalDate diaryDate,

        @Schema(
                description = "감정 점수 (1~5 범위)",
                example = "3"
        )
        int emotion,

        @Schema(
                description = "사용자의 한글 감정 문장",
                example = "춥고 마음도 조금 가라앉는 느낌이었다."
        )
        String feelingKo,

        @Schema(
                description = "GPT가 생성한 영어 감정 문장",
                example = "\"It's chilly, both outside and inside.\""
        )
        String feelingEn,

        @Schema(
                description = "오늘 실천한 습관 목록 (콤마로 구분된 문자열)",
                example = "물 2L 마시기, 명상, 운동"
        )
        String habitTags,

        @Schema(
                description = "오늘의 회고 본문",
                example = "차가운 날씨 때문에 손발도 마음도 차가운 하루였다."
        )
        String content,

        @Schema(
                description = "GPT가 생성한 요약 문장",
                example = "오늘은 전체적으로 차분하지만 다소 냉랭한 감정의 흐름이 있었다."
        )
        String summary,

        @Schema(
                description = "GPT가 생성한 개인 맞춤 피드백",
                example = "따뜻한 음료를 즐기며 마음까지 조금 더 편안하게 해보는 건 어떨까요?"
        )
        String feedback
) {}

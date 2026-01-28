package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 사용자의 감정·회고 활동 데이터를 유형별로 집계한 통계 응답 DTO.
 *
 * ✔ diaryEntryCount: 작성한 DiaryEntry 개수
 * ✔ gptSummaryCount: 생성된 GPT 요약(Summary) 개수
 * ✔ musicRecommendHistoryCount: 음악 추천 요청 기록 개수
 *
 * 프로필 화면·마이페이지 요약 통계에 활용됩니다.
 */
@Schema(description = "사용자의 감정·회고 활동을 유형별로 집계한 통계 응답 DTO")
@Builder
public record MemberDiaryStatsResponse(

        @Schema(description = "작성된 DiaryEntry 개수", example = "42")
        long diaryEntryCount,

        @Schema(description = "생성된 GPT Summary 개수", example = "31")
        long gptSummaryCount,

        @Schema(description = "음악 추천 요청 기록 개수", example = "18")
        long musicRecommendHistoryCount
) {}

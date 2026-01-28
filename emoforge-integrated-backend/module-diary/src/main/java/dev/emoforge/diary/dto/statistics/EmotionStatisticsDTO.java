package dev.emoforge.diary.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * EmotionStatisticsDTO
 *
 * 감정 통계 조회 API의 응답 DTO.
 * (EmotionStatisticsController.getEmotionStatistics() 사용)
 *
 * 특정 기간(startDate ~ endDate) 동안의 감정 점수 데이터를 기반으로
 * 다양한 통계 정보를 담아 반환한다.
 *
 * 제공 필드:
 *  ✔ averageEmotion       : 기간 동안의 평균 감정 점수
 *  ✔ emotionFrequency     : 감정 점수별 등장 횟수 (1~5)
 *  ✔ weeklyTrend          : 주차별 감정 점수 추세 (주간 평균 등)
 *  ✔ dayOfWeekAverage     : 요일별 평균 감정 점수 (월~일)
 *
 * 프론트엔드 통계 대시보드에서
 * - 감정 분포 차트
 * - 요일 패턴 분석
 * - 주간 감정 변화 그래프
 * 등에 활용되는 주요 데이터 구조이다.
 */
@Schema(description = "감정 통계 응답 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionStatisticsDTO {

    @Schema(
            description = "조회 기간 동안의 평균 감정 점수",
            example = "3.7"
    )
    private double averageEmotion;

    @Schema(
            description = """
                    감정 점수별 빈도수 Map.
                    key = 감정 점수 (1~5)
                    value = 해당 점수가 등장한 횟수
                    """,
            example = "{\"1\": 2, \"2\": 5, \"3\": 12, \"4\": 8, \"5\": 3}"
    )
    private Map<Integer, Long> emotionFrequency;

    @Schema(
            description = """
                    주차별 감정 추세 데이터.
                    각 주차의 평균 감정 점수 등을 포함한 WeeklyTrendDTO 리스트.
                    """,
            example = "[{\"week\": \"2025-W03\", \"averageEmotion\": 3.8}]"
    )
    private List<WeeklyTrendDTO> weeklyTrend;

    @Schema(
            description = """
                    요일별 평균 감정 점수.
                    key = 요일(MONDAY~SUNDAY)
                    value = 평균 감정 점수(double)
                    """,
            example = "{\"MONDAY\":3.5, \"TUESDAY\":3.8, \"SUNDAY\":4.2}"
    )
    private Map<DayOfWeek, Double> dayOfWeekAverage;
}

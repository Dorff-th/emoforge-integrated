package dev.emoforge.diary.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WeeklyTrendDTO
 *
 * 감정 통계 데이터 중 "주차별 감정 추세"를 나타내는 DTO.
 *
 * 사용 위치:
 *  - EmotionStatisticsDTO.weeklyTrend
 *  - EmotionStatisticsService.getEmotionStatistics()
 *
 * 제공 필드:
 *  ✔ weekLabel        : 주차 범위 라벨 (예: "07.01~07.07")
 *  ✔ averageEmotion   : 해당 주차의 평균 감정 점수
 *
 * 프론트에서는 선 그래프(Line Chart) 또는 주간 패턴 그래프에 사용된다.
 */
@Schema(description = "주차별 감정 추세 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyTrendDTO {
    @Schema(
            description = "주차 범위 라벨 (예: 날짜 구간)",
            example = "07.01~07.07"
    )
    private String weekLabel;  // 예: "07.01~07.07"

    @Schema(
            description = "해당 주차의 평균 감정 점수",
            example = "3.8"
    )
    private double averageEmotion;
}
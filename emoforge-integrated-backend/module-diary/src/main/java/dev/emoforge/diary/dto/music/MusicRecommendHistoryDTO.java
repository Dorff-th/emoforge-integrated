package dev.emoforge.diary.dto.music;

import dev.emoforge.diary.domain.MusicRecommendHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
/**
 * MusicRecommendHistoryDTO
 *
 * 특정 DiaryEntry에 대해 생성된 음악 추천 히스토리를 조회하는 DTO.
 *
 * 사용 위치:
 *  - MusicRecommendController.getRecommendations()
 *
 * 제공 데이터:
 *  ✔ historyId       : 추천 히스토리 ID
 *  ✔ keywordSummary  : 사용자의 감정 분석 결과 기반 키워드 요약
 *  ✔ songs           : 추천된 음악(YouTube 영상) 리스트
 *
 * 예시:
 * - "슬픔·지침 키워드 기반"
 * - "안정·회복 중심 음악 추천"
 *
 * 프론트에서는 "이미 추천된 음악 목록"을 표시하는 화면에 사용된다.
 */
@Schema(description = "음악 추천 히스토리 응답 DTO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicRecommendHistoryDTO {

    @Schema(
            description = "추천 히스토리 ID",
            example = "42"
    )
    private Long historyId;

    @Schema(
            description = "LangGraph-Service가 생성한 감정 키워드 요약",
            example = "Calm · Healing · Soft Mood"
    )
    private String keywordSummary;

    @Schema(
            description = "추천된 음악(YouTube) 리스트",
            example = """
                    [
                      {
                        "title": "Calm Piano Music",
                        "youtubeUrl": "https://youtu.be/xxxxx",
                        "artist": "Relax Studio"
                      }
                    ]
                    """
    )
    private List<MusicRecommendSongDTO> songs;

    public static MusicRecommendHistoryDTO fromEntity(MusicRecommendHistory history) {
        return MusicRecommendHistoryDTO.builder()
                .historyId(history.getId())
                .keywordSummary(history.getKeywordSummary())
                .songs(history.getSongs().stream()
                        .map(MusicRecommendSongDTO::fromEntity)
                        .toList())
                .build();
    }
}

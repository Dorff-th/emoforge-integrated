package dev.emoforge.diary.dto.music;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RecommendResultDTO
 *
 * LangGraph-Service 기반 음악 추천 결과를 프론트엔드로 반환하는 최종 DTO.
 *
 * 사용 위치:
 *  - MusicRecommendController.recommendMusic()
 *
 * 구성:
 *  ✔ keyword : LangGraph가 감정 기반으로 추출한 핵심 키워드
 *  ✔ songs   : 추천된 음악(YouTube 영상) 리스트 (SongDTO)
 *
 * 특징:
 *  - LangGraphResponse → RecommendResultDTO 변환 메서드 포함(from)
 *  - 프론트에서는 "AI 추천 음악 목록" UI 구성 시 사용됨
 */
@Schema(description = "AI 기반 음악 추천 결과 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResultDTO {

    @Schema(
            description = "LangGraph가 생성한 감정 기반 핵심 키워드",
            example = "Calm · Healing · Soft Mood"
    )
    private String keyword;

    @Schema(
            description = "추천된 YouTube 음악 리스트",
            example = """
                    [
                      {
                        "artist":"Relax Music Studio",
                        "title":"Healing Piano for Deep Rest",
                        "youtubeUrl":"https://youtu.be/abcd1234",
                        "thumbnailUrl":"https://i.ytimg.com/vi/abcd1234/hqdefault.jpg"
                      }
                    ]
                    """
    )
    private List<SongDTO> songs;

    // --------------------------------------------------------
    // 🔹 내부 static DTO (추천 음악 1곡)
    // --------------------------------------------------------
    @Schema(description = "추천 음악(단일 YouTube 영상) DTO")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SongDTO {

        @Schema(
                description = "아티스트 / YouTube 채널명",
                example = "Relax Music Studio"
        )
        private String artist;

        @Schema(
                description = "추천 음악 제목",
                example = "Healing Piano for Deep Rest"
        )
        private String title;

        @Schema(
                description = "YouTube 영상 URL",
                example = "https://youtu.be/abcd1234"
        )
        private String youtubeUrl;

        @Schema(
                description = "YouTube 영상 썸네일 URL",
                example = "https://i.ytimg.com/vi/abcd1234/hqdefault.jpg"
        )
        private String thumbnailUrl;
    }

    public static RecommendResultDTO from(LangGraphResponse response) {
        return RecommendResultDTO.builder()
                .keyword(response.getKeyword())
                .songs(response.getRecommendations().stream()
                        .map(r -> new SongDTO(r.getArtist(), r.getTitle(), r.getUrl(), r.getThumbnail()))
                        .collect(Collectors.toList()))
                .build();
    }
}
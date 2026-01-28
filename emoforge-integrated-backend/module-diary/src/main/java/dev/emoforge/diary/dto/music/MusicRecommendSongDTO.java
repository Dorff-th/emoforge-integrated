package dev.emoforge.diary.dto.music;

import dev.emoforge.diary.domain.MusicRecommendSong;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * MusicRecommendSongDTO
 *
 * 감정 기반 음악 추천 결과 중 "단일 음악(YouTube 영상)" 정보를 담는 DTO.
 *
 * 사용 위치:
 *  - MusicRecommendHistoryDTO.songs
 *
 * 구성 정보:
 *  ✔ title         : 추천 음악 제목
 *  ✔ artist        : 아티스트 / 채널명
 *  ✔ youtubeUrl    : YouTube 영상 URL
 *  ✔ thumbnailUrl  : YouTube 썸네일 URL
 *
 * LangGraph-Service에서 추천된 음악 한 건을 표현하며,
 * 프론트에서는 추천 목록 카드 UI의 요소로 사용된다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MusicRecommendSongDTO {

    @Schema(
            description = "음악 제목",
            example = "Relaxing Piano Music ～ Stress Relief"
    )
    private String title;

    @Schema(
            description = "아티스트 또는 YouTube 채널명",
            example = "Relax Music Studio"
    )
    private String artist;

    @Schema(
            description = "YouTube 영상 URL",
            example = "https://youtu.be/abcdEFGH1234"
    )
    private String youtubeUrl;

    @Schema(
            description = "YouTube 영상 썸네일 URL",
            example = "https://i.ytimg.com/vi/abcdEFGH1234/hqdefault.jpg"
    )
    private String thumbnailUrl;

    public static MusicRecommendSongDTO fromEntity(MusicRecommendSong song) {
        return MusicRecommendSongDTO.builder()
                .title(song.getSongTitle())
                .artist(song.getArtistName())
                .youtubeUrl(song.getYoutubeUrl())
                .thumbnailUrl(song.getThumbnailUrl())
                .build();
    }
}

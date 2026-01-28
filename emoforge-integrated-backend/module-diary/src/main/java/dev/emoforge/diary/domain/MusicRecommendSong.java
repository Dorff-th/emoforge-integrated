package dev.emoforge.diary.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "music_recommend_song")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicRecommendSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // History와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private MusicRecommendHistory history;

    @Column(name = "artist_name", length = 100)
    private String artistName;

    @Column(name = "song_title", length = 255)
    private String songTitle;

    @Column(name = "youtube_url", length = 500)
    private String youtubeUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private boolean liked = false;
}

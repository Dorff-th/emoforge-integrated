package dev.emoforge.diary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DiaryEntry별 감정 + 추천 세션(한 번의 LangGraph 호출)을 저장
 */
@Entity
@Table(name = "music_recommend_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicRecommendHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DiaryEntry와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_entry_id", nullable = false)
    private DiaryEntry diaryEntry;

    @Column(name = "member_uuid", length = 36, nullable = false)
    private String memberUuid;

    @Column(name = "emotion_score", nullable = false)
    private Integer emotionScore;

    @Column(name = "feeling_ko", length = 255)
    private String feelingKo;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "keyword_summary", length = 255)
    private String keywordSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicRecommendSong> songs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public void addSong(MusicRecommendSong song) {
        songs.add(song);
        song.setHistory(this);
    }
}

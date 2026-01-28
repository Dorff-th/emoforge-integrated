package dev.emoforge.diary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_artist_preference")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberArtistPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_uuid", length = 36, nullable = false)
    private String memberUuid;

    @Column(name = "artist_name", length = 100, nullable = false)
    private String artistName;

    // 사용자가 좋아요를 누를수록 증가시키는 개념 (추천가중치)
    //@Column(name = "preference_score", precision = 3, scale = 2)
    @Column(name = "preference_score")
    private Double preferenceScore;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

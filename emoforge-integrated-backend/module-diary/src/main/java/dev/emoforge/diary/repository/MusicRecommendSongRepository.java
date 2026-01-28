package dev.emoforge.diary.repository;

import dev.emoforge.diary.domain.MusicRecommendSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 개별 추천 곡을 관리하는 Repository
 */
public interface MusicRecommendSongRepository extends JpaRepository<MusicRecommendSong, Long> {

    // ✅ 특정 추천 세션(historyId)에 포함된 모든 곡 조회
    List<MusicRecommendSong> findByHistoryId(Long historyId);

    // ✅ 좋아요 누른 곡들만 조회
    List<MusicRecommendSong> findByLikedTrue();

    // ✅ 특정 아티스트의 곡 추천 이력 조회
    List<MusicRecommendSong> findByArtistName(String artistName);

    /**
     * @param historyId
     * 특정 히스토리(music_recommend_history)에 연결된 곡들을 일괄 삭제
     * 시 cascade = CascadeType.ALL이 걸려 있으면 자동으로 삭제됨
     */
    @Modifying
    @Query("DELETE FROM MusicRecommendSong s WHERE s.history.id = :historyId")
    void deleteAllByHistoryId(Long historyId);
}

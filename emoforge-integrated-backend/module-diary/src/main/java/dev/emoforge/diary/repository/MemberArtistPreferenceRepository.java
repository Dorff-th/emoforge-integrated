package dev.emoforge.diary.repository;

import dev.emoforge.diary.domain.MemberArtistPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자별 선호 아티스트 관리용 Repository
 */
public interface MemberArtistPreferenceRepository extends JpaRepository<MemberArtistPreference, Long> {

    // ✅ 특정 사용자(memberUuid)의 모든 선호 아티스트 조회
    List<MemberArtistPreference> findByMemberUuidOrderByPreferenceScoreDesc(String memberUuid);

    // ✅ 특정 사용자 + 아티스트 조합 존재 여부 확인 (중복 방지)
    Optional<MemberArtistPreference> findByMemberUuidAndArtistName(String memberUuid, String artistName);

    // ✅ 특정 아티스트를 선호하는 전체 사용자 조회 (통계용)
    List<MemberArtistPreference> findByArtistName(String artistName);
}

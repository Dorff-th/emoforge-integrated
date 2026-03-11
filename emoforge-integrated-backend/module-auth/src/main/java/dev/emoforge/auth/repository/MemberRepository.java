package dev.emoforge.auth.repository;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

    Optional<Member> findByUuid(String uuid);

    // 2026-03-11: Added member deletion by uuid for admin purge flow.
    void deleteByUuid(String uuid);

    List<Member> findAllByOrderByCreatedAtDesc();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.status = :status WHERE m.uuid = :uuid")
    int updateStatusByUuid(@Param("uuid") String uuid, @Param("status") MemberStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = :deleted WHERE m.uuid = :uuid")
    int updateDeletedByUuid(@Param("uuid") String uuid, @Param("deleted") boolean deleted);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = true, m.deletedAt = :deletedAt WHERE m.uuid = :uuid")
    int markMemberAsDeleted(
            @Param("uuid") String uuid,
            @Param("deletedAt") LocalDateTime deletedAt
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = false, m.deletedAt = NULL WHERE m.uuid = :uuid")
    int cancelMemberDeletion(@Param("uuid") String uuid);
}

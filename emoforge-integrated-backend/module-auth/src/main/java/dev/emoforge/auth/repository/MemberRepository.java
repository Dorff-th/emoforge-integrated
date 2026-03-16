package dev.emoforge.auth.repository;

import dev.emoforge.auth.dto.admin.AdminMemberListDTO;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //legacy
    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(
            value = """
                    SELECT m.*
                    FROM member m
                    WHERE (:nickname IS NULL OR :nickname = '' OR m.nickname LIKE CONCAT('%', :nickname, '%'))
                      AND (:deleted IS NULL OR m.deleted = :deleted)
                      AND (:startDate IS NULL OR DATE(m.created_at) >= :startDate)
                      AND (:endDate IS NULL OR DATE(m.created_at) <= :endDate)
                    ORDER BY m.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM member m
                    WHERE (:nickname IS NULL OR :nickname = '' OR m.nickname LIKE CONCAT('%', :nickname, '%'))
                      AND (:deleted IS NULL OR m.deleted = :deleted)
                      AND (:startDate IS NULL OR DATE(m.created_at) >= :startDate)
                      AND (:endDate IS NULL OR DATE(m.created_at) <= :endDate)
                    """,
            nativeQuery = true
    )
    Page<Member> searchMembers(
            @Param("nickname") String nickname,
            @Param("deleted") Boolean deleted,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

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

    //2026.03.16 추가 - 토큰 인증할때 status값을 먼저 조회해서 ACTVICE / INCATVICE 상태를 파악하는 용도
    @Query("""
       select m.status
       from Member m
       where m.uuid = :uuid
       """)
    Optional<MemberStatus> findStatusByUuid(@Param("uuid") String uuid);
}

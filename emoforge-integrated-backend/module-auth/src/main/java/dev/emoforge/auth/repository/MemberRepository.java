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

    //카카오 id 로 사용자 찾기
    Optional<Member> findByKakaoId(Long kakaoId);
    // ✅ UUID 로 회원 조회
    Optional<Member> findByUuid(String uuid);

    //관리자 기능 -  전체 회원 목록 (정렬: 생성일 내림차순)
    List<Member> findAllByOrderByCreatedAtDesc();

    // 관리자 기능 -  회원 상태 변경 (ACTIVE / INACTIVE)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.status = :status WHERE m.uuid = :uuid")
    int updateStatusByUuid(@Param("uuid") String uuid, @Param("status") MemberStatus status);

    // 관리자 기능 (혹은 사용자가 회원탈퇴를 신청할때)-  탈퇴 여부 변경 (deleted = 0 or 1) - 검토중
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = :deleted WHERE m.uuid = :uuid")
    int updateDeletedByUuid(@Param("uuid") String uuid, @Param("deleted") boolean deleted);

    //탈퇴 요청
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = true, m.deletedAt = :deletedAt WHERE m.uuid = :uuid")
    int markMemberAsDeleted(
            @Param("uuid") String uuid,
            @Param("deletedAt") LocalDateTime deletedAt
    );

    //탈퇴 취소
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.deleted = false, m.deletedAt = NULL WHERE m.uuid = :uuid")
    int cancelMemberDeletion(@Param("uuid") String uuid);
}

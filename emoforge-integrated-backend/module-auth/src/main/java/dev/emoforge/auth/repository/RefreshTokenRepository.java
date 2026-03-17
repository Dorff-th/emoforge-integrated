package dev.emoforge.auth.repository;

import dev.emoforge.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 토큰 조회
     *  - refresh 요청 들어올 때 쿠키 -> hash -> DB 조회
     * */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * 사용자 기준 조회
     *  - 특정 유저의 모든 토큰 조회(멀티 로그인 관리), 보안대응(전체 로그아웃)
     * */
    List<RefreshToken> findByMemberUuid(String memberUuid);

    /**
     * 토큰 삭제
     * */
    void deleteByTokenHash(String tokenHash);

    /**
     * 사용자 전체 로그아웃
     */
    void deleteByMemberUuid(String memberUuid);

    //굳이 전체 엔티티 필요 없을 때
    boolean existsByTokenHash(String tokenHash);

    /**
     * 만료된 토큰 삭제 (maintenance 실행할때 사용 예정 -> emoforge-maintenance 에서 구현예정)
     */
    /*@Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiredAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);*/
}

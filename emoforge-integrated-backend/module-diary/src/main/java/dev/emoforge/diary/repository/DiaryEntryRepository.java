package dev.emoforge.diary.repository;


import dev.emoforge.diary.domain.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    @EntityGraph(attributePaths = {"gptSummary"})
    Page<DiaryEntry> findByMemberUuid(String memberUuid, Pageable pageable);

    //연월 범위로 조회
    List<DiaryEntry> findByMemberUuidAndDiaryDateBetweenOrderByDiaryDateAsc(String memberUuid, LocalDate startDate, LocalDate endDate);

    List<DiaryEntry> findByMemberUuidAndDiaryDate(String memberUuid, LocalDate date);

    List<DiaryEntry> findByMemberUuidAndDiaryDateBetween(String memberUuid, LocalDate startDate, LocalDate endDate);

    //Optional<DiaryEntry> findByMemberAndDiaryDate(Member member, LocalDate diaryDate);
    Optional<DiaryEntry> findTopByMemberUuidAndDiaryDateOrderByCreatedAtDesc(String memberUuid, LocalDate diaryDate);

    //감정 & 회고 삭제
    //그날 남은 회고 수 확인용
    long countByMemberUuidAndDiaryDate(String memberUuid, LocalDate date);

    //하루 전체 회고 삭제용 (예: “그날 회고 전체 삭제” 버튼)
    @Transactional
    @Modifying
    @Query("DELETE FROM DiaryEntry d WHERE d.memberUuid = :memberUuid AND d.diaryDate = :date")
    void deleteAllByMemberUuidAndDate(@Param("memberUuid") String memberUuid, @Param("date") LocalDate date);

    //특정 사용자(memberUuid)의 감정&회고 입력 개수 조회(auth-frontend의 프로필 화면에서 조회 용도)
    int countByMemberUuid(@Param("memberUuid") String memberUuid);
}

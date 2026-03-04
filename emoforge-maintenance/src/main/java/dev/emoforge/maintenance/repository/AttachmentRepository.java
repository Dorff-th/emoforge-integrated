package dev.emoforge.maintenance.repository;

import dev.emoforge.maintenance.domain.Attachment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // ✅ TEMP 에디터 이미지 배치 조회 (id 기반)
    @Query(value = """
        select *
        from attachment
        where upload_type = 'EDITOR_IMAGE'
          and status = 'TEMP'
          and post_id is null
          and uploaded_at < :threshold
          and id > :lastId
        order by id asc
        limit :limit
        """, nativeQuery = true)
    List<Attachment> findTempEditorBatch(
            @Param("threshold") LocalDateTime threshold,
            @Param("lastId") long lastId,
            @Param("limit") int limit
    );

    // ✅ 삭제 직전 조건 재검증 (동시성 안전장치) -- 미사용
    /*@Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """
        delete from attachment
        where id = :id
          and upload_type = 'EDITOR_IMAGE'
          and status = 'TEMP'
          and post_id is null
          and uploaded_at < :threshold
        """, nativeQuery = true)
    int deleteTempEditorByIdWithGuard(
            @Param("id") long id,
            @Param("threshold") LocalDateTime threshold
    );*/

    //  profile
    @Query(value = """
    select *
    from attachment a
    where a.upload_type = 'PROFILE_IMAGE'
      and a.id not in (
            select max(id)
            from attachment
            where upload_type = 'PROFILE_IMAGE'
            group by member_uuid
      )
      and a.id > :lastId
      and a.uploaded_at < :threshold
    order by a.id asc
    limit :limit
    """, nativeQuery = true)
    List<Attachment> findOldProfileImagesBatch(
            @Param("threshold") LocalDateTime threshold,
            @Param("lastId") long lastId,
            @Param("limit") int limit
    );

    //삭제는 editor, profile attachment 공통
    @Modifying
    @Transactional
    @Query(value = """
     delete from attachment where id = :id
        """, nativeQuery = true)
    int deleteOldAttachment(
            @Param("id") long id
    );


    // attachment
    @Query(value = """
    select *
    from attachment
    where upload_type = 'ATTACHMENT'
      and post_id is null
      and uploaded_at < :threshold
      and id > :lastId
    order by id asc
    limit :limit
    """, nativeQuery = true)
    List<Attachment> findOrphanAttachmentsBatch(
            @Param("threshold") LocalDateTime threshold,
            @Param("lastId") long lastId,
            @Param("limit") int limit
    );

    /*@Modifying
    @Transactional
    @Query(value = """
    delete from attachment
    where id = :id
      and upload_type = 'ATTACHMENT'
      and post_id is null
      and uploaded_at < :threshold
    """, nativeQuery = true)
    int deleteOrphanAttachmentWithGuard(
            @Param("id") long id,
            @Param("threshold") LocalDateTime threshold
    );*/

    //db attachment 에 등록된 전체 파일경로 조회(db에 등록되지 않는 가비지 파일 정리 용도)
    @Query("select a.fileUrl from Attachment a")
    List<String> findAllFileUrls();
}
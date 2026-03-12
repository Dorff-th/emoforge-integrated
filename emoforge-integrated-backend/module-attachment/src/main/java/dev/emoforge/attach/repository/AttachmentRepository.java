package dev.emoforge.attach.repository;

import dev.emoforge.attach.domain.Attachment;
import dev.emoforge.attach.domain.AttachmentStatus;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.dto.UploadTypeCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByPostId(Long postId);

    List<Attachment> findByPostIdAndDeletedFalse(Long postId);

    Optional<Attachment> findTopByMemberUuidAndUploadTypeAndDeletedFalseOrderByUploadedAtDesc(
            String memberUuid, UploadType uploadType
    );

    long countByPostIdAndUploadTypeAndDeletedFalse(Long postId, UploadType uploadType);

    long countByUploadTypeAndDeletedFalse(UploadType uploadType);

    List<Attachment> findByMemberUuidAndDeletedFalse(String memberUuid);

    // 2026-03-11: Added profile image lookup for admin member purge.
    List<Attachment> findAllByMemberUuidAndUploadType(String memberUuid, UploadType uploadType);

    @Query("SELECT a.postId, COUNT(a) FROM Attachment a " +
            "WHERE a.postId IN :postIds AND a.uploadType = :uploadType " +
            "GROUP BY a.postId")
    List<Object[]> countByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("uploadType") UploadType uploadType
    );

    @Query("SELECT a FROM Attachment a WHERE a.postId = :postId AND a.uploadType = :uploadType")
    List<Attachment> findByPostId(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType);

    @Modifying
    @Transactional
    @Query("UPDATE Attachment a " +
            "SET a.postId = :postId, a.attachmentStatus = :status " +
            "WHERE a.tempKey = :tempKey")
    int updatePostIdAndConfirmByTempKey(@Param("postId") Long postId,
                                        @Param("status") AttachmentStatus status,
                                        @Param("tempKey") String tempKey);

    @Modifying
    @Query("UPDATE Attachment a SET a.attachmentStatus = 'CONFIRMED' " +
            "WHERE a.postId = :postId AND a.uploadType = :uploadType AND a.publicUrl IN :fileUrls")
    int confirmEditorImages(@Param("postId") Long postId,
                            @Param("uploadType") UploadType uploadType,
                            @Param("fileUrls") List<String> fileUrls);

    @Modifying
    @Query("DELETE FROM Attachment a " +
            "WHERE a.postId = :postId AND a.uploadType = :uploadType " +
            "AND a.publicUrl NOT IN :fileUrls")
    int deleteUnusedEditorImages(@Param("postId") Long postId,
                                 @Param("uploadType") UploadType uploadType,
                                 @Param("fileUrls") List<String> fileUrls);

    void deleteByPostIdIn(List<Long> postIds);

    void deleteByPostId(Long postId);

    Optional<Attachment> findTopByMemberUuidAndUploadTypeOrderByUploadedAtDesc(String memberUuid, UploadType uploadType);

    @Query("""
        SELECT new dev.emoforge.attach.dto.UploadTypeCountResponse(
            a.uploadType,
            COUNT(a)
        )
        FROM Attachment a
        WHERE a.memberUuid = :memberUuid
          AND a.uploadType IN (:types)
        GROUP BY a.uploadType
    """)
    List<UploadTypeCountResponse> countByMemberAndUploadTypes(
            @Param("memberUuid") String memberUuid,
            @Param("types") List<UploadType> types
    );

    @Query("SELECT a FROM Attachment a WHERE a.postId = :postId AND a.uploadType = :uploadType")
    List<Attachment> findByPostIdAndUploadType(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType);
}

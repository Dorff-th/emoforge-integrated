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

    // 게시글별 첨부 조회 (삭제 제외)
    List<Attachment> findByPostIdAndDeletedFalse(Long postId);

    // 특정 타입(예: 프로필 이미지) 최신 1개
    Optional<Attachment> findTopByMemberUuidAndUploadTypeAndDeletedFalseOrderByUploadedAtDesc(
            String memberUuid, UploadType uploadType
    );

    // 게시글별/타입별 개수 (예: 일반 첨부 3개 제한 체크)
    long countByPostIdAndUploadTypeAndDeletedFalse(Long postId, UploadType uploadType);

    // 소유자 기준 전체 조회 (필요 시)
    List<Attachment> findByMemberUuidAndDeletedFalse(String memberUuid);

    //post에 첨부된 파일 개수 구하기(Post-Service 에서 bbf로직에서 사용)
    @Query("SELECT a.postId, COUNT(a) FROM Attachment a " +
            "WHERE a.postId IN :postIds AND a.uploadType = :uploadType " +
            "GROUP BY a.postId")
    List<Object[]> countByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("uploadType") UploadType uploadType
    );

    //post에 첨부된 파일 메타 정보 구하기(Post-Service 에서 bbf로직에서 사용)
    @Query("SELECT a FROM Attachment a WHERE a.postId = :postId AND a.uploadType = :uploadType")
    List<Attachment> findByPostId(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType);

    //Post 등록이 성공하면 postId에 가져온 postId값과 status를 CONFIRMED로 업데이트 한다.
    @Modifying
    @Transactional
    @Query("UPDATE Attachment a " +
            "SET a.postId = :postId, a.attachmentStatus = :status " +
            "WHERE a.tempKey = :tempKey")
    int updatePostIdAndConfirmByTempKey(@Param("postId") Long postId,
                                        @Param("status") AttachmentStatus status ,
                                        @Param("tempKey") String tempKey);


    // 본문에 남아있는 이미지들만 CONFIRMED 상태로 변경
    @Modifying
    @Query("UPDATE Attachment a SET a.attachmentStatus = 'CONFIRMED' " +
            "WHERE a.postId = :postId AND a.uploadType = :uploadType AND a.publicUrl IN :fileUrls")
    int confirmEditorImages(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType, @Param("fileUrls") List<String> fileUrls);

    // 본문에 없는 이미지들은 삭제
    @Modifying
    @Query("DELETE FROM Attachment a " +
            "WHERE a.postId = :postId AND a.uploadType = :uploadType " +
            "AND a.publicUrl NOT IN :fileUrls")
    int deleteUnusedEditorImages(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType,  @Param("fileUrls") List<String> fileUrls);


    //Post 삭제될때 하위 Attachment 모두 삭제 - 관리자 기능
    void deleteByPostIdIn(List<Long> postIds);

    //특정 Post 삭제될때 Attachment 삭제
    void deleteByPostId(Long postId);

    /**
     * 특정 사용자의 최신 프로필 이미지를 조회합니다.
     * - memberUuid 기준
     * - uploadType 기준 (예: PROFILE_IMAGE)
     * - 업로드 시점(uploadedAt) 기준으로 최신 1건 반환
     */
    Optional<Attachment> findTopByMemberUuidAndUploadTypeOrderByUploadedAtDesc(String memberUuid, UploadType uploadType);

    /**
     * 특정 사용자(memberUuid)에 대한 업로드 타입별 파일 수 집계
     */
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

}

package dev.emoforge.post.repository;

import dev.emoforge.post.domain.Comment;
import dev.emoforge.post.dto.query.CommentViewProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.postId = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    //코멘트일괄삭제 (Post가 삭제될때 해당 코멘트모두 삭제) - 관리자 기능에서 사용
    void deleteByPostIdIn(List<Long> postIds);

    // 특정 post 의 Comment 삭제(사용자 ROLE)
    void deleteByPostId(Long postId);

    // 개별 댓글이 postId + commentId 조건으로 존재하는지 확인 가능
    boolean existsByIdAndPostId(Long id, Long postId);

    /**
     * 특정 회원(member_uuid)이 작성한 comment 개수를 조회한다.
     */
    int countByMemberUuid(@Param("memberUuid") String memberUuid);

    /* ========================== Emoforge 통합 작업에 필요한 신규 쿼리 메서드 */
    @Query(value = """
            SELECT
                  c.id                    AS id,
                  c.post_id               AS postId,
                  m.uuid                  AS memberUuid,
                  m.nickname              AS nickname,
                  c.content               AS content,
                  c.created_at            AS createdAt,
                  c.updated_at            AS updatedAt,
                  pi.public_url           AS profileImageUrl
                  FROM comment c
                  JOIN member m
                  ON c.member_uuid = m.uuid
                  LEFT JOIN (
                          SELECT a.member_uuid, a.public_url
                          FROM attachment a
                                  WHERE a.upload_type = 'PROFILE_IMAGE'
                                  AND a.id = (
                                  SELECT MAX(a2.id)
                  FROM attachment a2
                  WHERE a2.member_uuid = a.member_uuid
                  AND a2.upload_type = 'PROFILE_IMAGE'
                          )
                          ) pi
                  ON pi.member_uuid = m.uuid
                  WHERE c.post_id = 1
                  ORDER BY c.created_at ASC
            """, nativeQuery = true)
    List<CommentViewProjection> findCommentsByPostId(@Param("postId") Long postId);

}

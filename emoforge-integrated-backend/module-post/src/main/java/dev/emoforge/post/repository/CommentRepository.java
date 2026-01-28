package dev.emoforge.post.repository;

import dev.emoforge.post.domain.Comment;
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
}

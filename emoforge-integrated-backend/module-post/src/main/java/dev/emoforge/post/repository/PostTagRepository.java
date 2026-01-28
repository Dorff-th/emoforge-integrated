package dev.emoforge.post.repository;


import dev.emoforge.post.domain.PostTag;
import dev.emoforge.post.dto.internal.TagDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    //postId값으로 태그 조회
    @Query("SELECT new dev.emoforge.post.dto.internal.TagDTO(pt, t.name) " +
            "FROM PostTag pt " +
            "JOIN pt.tag t " +
            "WHERE pt.post.id = :postId")
    List<TagDTO> findTagsByPostId(@Param("postId") Long postId);

    // 수정할 게시글(postId)에서 삭제할 tag id들을 찾아서 삭제한다.
    void deleteByPostIdAndTagIdIn(Long postId, List<Long> tagIds);

    //PostTag일괄 삭제(Post 삭제될때 하위 PostTag 모두 삭제) - 관리자 기능
    void deleteByPostIdIn(List<Long> postIds);

    //특정 Post 삭제시 PostTag삭제
    void deleteByPostId(Long postId);
}

package dev.emoforge.post.repository;

import dev.emoforge.post.admin.dto.AdminPostListItemDTO;
import dev.emoforge.post.domain.Post;
import dev.emoforge.post.dto.internal.PostDetailDTO;
import dev.emoforge.post.dto.internal.PostSimpleDTO;
import dev.emoforge.post.dto.internal.PostUpdateDTO;
import dev.emoforge.post.dto.query.PostDetailViewProjection;
import dev.emoforge.post.dto.query.PostListItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {




    /* =============================================== */

    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageable);

    Page<Post> findAllByMemberUuid(String memberUuid, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Post p SET p.categoryId = :defaultCategoryId WHERE p.categoryId = :categoryId")
    int movePostsToDefaultCategory(@Param("categoryId") Long categoryId, @Param("defaultCategoryId") Long defaultCategoryId);

    void deleteByIdIn(List<Long> postIds);

    //게시글 목록 조회 - bbf 계층이 제거되면 폐기예정
    @Query("SELECT new dev.emoforge.post.dto.internal.PostSimpleDTO(" +
        "p.id, p.title, p.createdAt, c.name, p.memberUuid, " +
        "(SELECT COUNT(cm) FROM Comment cm WHERE cm.postId = p.id)" +
        ") " +
        "FROM Post p " +
        "LEFT JOIN Category c ON p.categoryId = c.id " +
        "ORDER BY p.id DESC")
    Page<PostSimpleDTO> findAllPosts(Pageable pageable);

    // 특정 태그 조회 - bbf 계층이 제거되면 폐기예정
    @Query("SELECT new dev.emoforge.post.dto.internal.PostSimpleDTO(" +
        "p.id, p.title, p.createdAt, c.name, p.memberUuid, " +
        "(SELECT COUNT(cm) FROM Comment cm WHERE cm.postId = p.id)" +
        ") " +
        "FROM Post p " +
        "LEFT JOIN Category c ON p.categoryId = c.id " +
        "JOIN p.postTags pt " +
        "JOIN pt.tag t " +
        "WHERE t.name = :tagName " +
        "ORDER BY p.id DESC")
    Page<PostSimpleDTO> findAllPostsByTag(@Param("tagName") String tagName, Pageable pageable);

    /**
     *  게시물 상세 조회 - bbf 계층이 제거되면 폐기예정
     * @param postId
     * @return Optional<PostDetailDTO>
     */
    @Query("""
        SELECT new dev.emoforge.post.dto.internal.PostDetailDTO(
            p.id,
            p.memberUuid,
            p.title,
            p.content,
            p.createdAt,
            p.updatedAt,
            p.categoryId,
            (SELECT c.name FROM Category c WHERE c.id = p.categoryId)
        )
        FROM Post p
        WHERE p.id = :postId
    """)
    Optional<PostDetailDTO> findPostDetail(@Param("postId") Long postId);

    /**
     * 게시물을 수정한다.
     * @param dto
     * @return int
     */
    @Modifying
    @Transactional
    @Query("UPDATE Post a SET a.title = :#{#dto.title}, a.content =:#{#dto.content}, categoryId =:#{#dto.categoryId}, a.updatedAt =:#{#dto.updatedAt} " +
        "WHERE a.id = :#{#dto.id}")
    int updatePostById(@Param("dto") PostUpdateDTO dto);

    /**
     * 특정 회원(member_uuid)이 작성한 post 개수를 조회한다.
     */
    int countByMemberUuid(@Param("memberUuid") String memberUuid);

    /**
     * 특정 회원(member_uuid)이 작성한 오늘 작성한 post 개수를 조회한다.
     */
    int countByMemberUuidAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            @Param("memberUuid") String memberUuid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    /* ====================================== Emoforge 통합에 필요한 신규 쿼리   ========================================== */
    @Query(
            value = """
                SELECT
                  p.id                AS postId,
                  p.title             AS title,
                  p.created_at        AS createdAt,
            
                  COALESCE(c.name, 'unknown') AS categoryName,
                  COALESCE(m.nickname, '[탈퇴 사용자]')          AS nickname,
            
                  (
                    SELECT COUNT(*)
                    FROM comment cm
                    WHERE cm.post_id = p.id
                  )                   AS commentCount,
            
                  (
                    SELECT COUNT(*)
                    FROM attachment a
                    WHERE a.post_id = p.id
                      AND a.upload_type = 'ATTACHMENT'
                      AND a.deleted = 0
                  )                   AS attachmentCount
            
                FROM post p
                LEFT JOIN member m
                  ON p.member_uuid = m.uuid
                LEFT JOIN category c
                  ON p.category_id = c.id
            
                /* tag 조건용 JOIN */
                LEFT JOIN post_tag pt
                  ON pt.post_id = p.id
                LEFT JOIN tag t
                  ON t.id = pt.tag_id
            
                WHERE (:tagName IS NULL OR t.name = :tagName)
            
                GROUP BY
                  p.id, p.title, p.created_at,
                  c.name,
                  m.nickname
            
                ORDER BY p.id DESC
              """,
            countQuery = """
                SELECT COUNT(DISTINCT p.id)
                FROM post p
                LEFT JOIN post_tag pt
                  ON pt.post_id = p.id
                LEFT JOIN tag t
                  ON t.id = pt.tag_id
                WHERE (:tagName IS NULL OR t.name = :tagName)
              """,
            nativeQuery = true
    )
    Page<PostListItemProjection> findPostList(
            @Param("tagName") String tagName,
            Pageable pageable
    );


    //2026 0306 관리자 목록 쿼리 추가
    @Query("""
        SELECT new dev.emoforge.post.admin.dto.AdminPostListItemDTO(
                p.id,
                p.title,
                p.viewCount,
                p.createdAt,
                p.updatedAt,
                p.memberUuid,
                p.categoryId,
                COALESCE(c.name, 'Unknown'),
                p.adminModifiedAt,
                p.adminModifiedBy,
                COALESCE(m.nickname, '탈퇴회원')
            )
            FROM Post p
            LEFT JOIN Member m
                ON p.memberUuid = m.uuid
            LEFT JOIN Category c
                  ON p.categoryId = c.id    
            WHERE (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY p.id DESC
    """)
    Page<AdminPostListItemDTO> findAdminPostList(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    //2026.03.06  : admin_modified_by 조회컬럼 추가
    @Query(value = """
        SELECT
              p.id               AS id,
              m.uuid             AS memberUuid,
              m.nickname         AS nickname,
              p.title            AS title,
              p.content          AS content,
              p.created_at       AS createdAt,
              p.updated_at       AS updatedAt,
              p.admin_modified_at AS adminModifiedAt,
              p.admin_modified_by AS adminModifiedBy,
              am.nickname        AS adminModifiedByNickname,
              c.id               AS categoryId,
              COALESCE(c.name, 'unknown') AS categoryName
          FROM post p
          LEFT JOIN member m
              ON p.member_uuid = m.uuid
          LEFT JOIN member am
              ON p.admin_modified_by = am.uuid
          LEFT JOIN category c
              ON p.category_id = c.id
          WHERE p.id = :postId
      """, nativeQuery = true)
    Optional<PostDetailViewProjection> findPostDetailViewById(@Param("postId") Long postId);


}

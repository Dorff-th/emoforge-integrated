package dev.emoforge.post.mapper;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSearchResultDTO;
import dev.emoforge.post.dto.internal.SearchFilterDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 게시글 검색 MyBatis Mapper.
 *
 * 공용 Query 계층으로, Context(Admin/User) 구분 없이
 * 조건 기반 게시글 검색 쿼리를 정의한다.
 *
 * 설계 원칙:
 * - Context 중립적 (Admin/User 구분 없음)
 * - 검색 조건(SearchFilterDTO)과 페이징(PageRequestDTO)만 전달받음
 * - Facade 계층에서 해석된 조건만 사용
 */
@Mapper
public interface SearchMapper {

    /**
     * 조건 기반 게시글 목록 검색 (페이징 포함)
     *
     * @param filter 검색 조건 (keyword, titleChecked, contentChecked, categoryId 등)
     * @param page 페이징 정보 (page, size, sort, direction)
     * @return 게시글 목록
     */
    List<PostSearchResultDTO> searchPostsByFilter(
        @Param("filter") SearchFilterDTO filter,
        @Param("page") PageRequestDTO page
    );

    /**
     * 조건 기반 게시글 검색 결과 개수
     *
     * @param filter 검색 조건
     * @return 전체 개수
     */
    int countPostsByFilter(@Param("filter") SearchFilterDTO filter);
}

package dev.emoforge.post.service.internal;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSearchResultDTO;
import dev.emoforge.post.dto.internal.SearchFilterDTO;
import dev.emoforge.post.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 검색 Query 서비스.
 *
 * 순수 DB 조회 계층으로, 외부 서비스(Auth, Attach) 호출 없이
 * 게시글 검색 쿼리만 담당한다.
 *
 * 설계 원칙:
 * - 공용 Query 계층: Context(Admin/User) 구분 없음
 * - 검색 조건(SearchFilterDTO)과 페이징(PageRequestDTO)만 전달받음
 * - Facade 계층에서 해석된 조건만 사용
 * - 외부 서비스 호출 없음
 * - MyBatis(SearchMapper) 캡슐화
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostSearchQueryService {

    private final SearchMapper searchMapper;

    /**
     * 조건 기반 게시글 검색 (페이징 포함)
     *
     * @param filter 검색 조건 (keyword, titleChecked, contentChecked, categoryId 등)
     * @param pageRequest 페이징 정보 (page, size, sort, direction)
     * @return 검색 결과 (목록 + 전체 개수)
     */
    @Transactional(readOnly = true)
    public PostSearchResult searchPosts(SearchFilterDTO filter, PageRequestDTO pageRequest) {
        log.debug("Post search: keyword={}, titleChecked={}, contentChecked={}, categoryId={}, page={}, size={}",
            filter.keyword(),
            filter.titleChecked(),
            filter.contentChecked(),
            filter.categoryId(),
            pageRequest.page(),
            pageRequest.size()
        );

        // 1. 목록 조회
        List<PostSearchResultDTO> posts = searchMapper.searchPostsByFilter(filter, pageRequest);

        // 2. 전체 개수 조회
        int totalCount = searchMapper.countPostsByFilter(filter);

        log.debug("Post search result: found {} posts (total: {})", posts.size(), totalCount);

        return new PostSearchResult(posts, totalCount);
    }

    /**
     * 게시글 검색 결과 래퍼 클래스.
     *
     * 검색된 게시글 목록과 전체 개수를 함께 반환한다.
     */
    public record PostSearchResult(
        /** 검색된 게시글 목록 */
        List<PostSearchResultDTO> posts,
        /** 전체 개수 (페이징 계산용) */
        int totalCount
    ) {}
}

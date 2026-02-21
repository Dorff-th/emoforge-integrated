package dev.emoforge.post.service.query;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.internal.PostSearchFilter;
import dev.emoforge.post.dto.internal.PostSearchRequest;
import dev.emoforge.post.dto.query.PostSearchResultDTO;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import dev.emoforge.post.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchService {

    private final SearchMapper searchMapper;
    private static final int PAGE_BLOCK_SIZE = 10;

    public PageResponseDTO<PostSearchResultDTO> search(PostSearchRequest request, PostSearchFilter filter) {

        applyDefaultSearchScopeIfEmpty(filter);

        List<PostSearchResultDTO> content =
                searchMapper.searchPostsByFilter(filter, request.getPage());

        int total =
                searchMapper.countPostsByFilter(filter);

        PageResponseDTO pageResponseDTO = new PageResponseDTO(request.getPage(), total, content, PAGE_BLOCK_SIZE);

        return pageResponseDTO;
    }

    /**
     * 체크박스 아무것도 선택 안했으면 전체 검색으로 보정
     */
    private void applyDefaultSearchScopeIfEmpty(PostSearchFilter filter) {

        boolean noScopeSelected =
                !Boolean.TRUE.equals(filter.getTitleChecked()) &&
                        !Boolean.TRUE.equals(filter.getContentChecked()) &&
                        !Boolean.TRUE.equals(filter.getCommentChecked()) &&
                        !Boolean.TRUE.equals(filter.getCategoryChecked()) &&
                        !Boolean.TRUE.equals(filter.getAuthorChecked());

        if (noScopeSelected) {
            filter.setTitleChecked(true);
            filter.setContentChecked(true);
            filter.setCommentChecked(true);
            filter.setCategoryChecked(true);
            filter.setAuthorChecked(true);
        }
    }
}
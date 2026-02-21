package dev.emoforge.post.service.query;

import dev.emoforge.app.EmoforgeApplication;
import dev.emoforge.post.dto.internal.*;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import dev.emoforge.post.dto.query.PostSearchResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EmoforgeApplication.class)
@Transactional
class PostSearchServiceTest {

    @Autowired
    private PostSearchService postSearchService;

    @Test
    void 키워드_하나로_전체범위_검색된다() {

        PostSearchFilter filter = new PostSearchFilter();
        filter.setKeyword("스크린샷");
        // 일부러 체크 안함 → 전체 검색으로 보정 기대

        PageRequestDTO page = new PageRequestDTO(0, 10, "id", SortDirection.DESC);

        PostSearchRequest request = new PostSearchRequest(filter, page);

        PageResponseDTO<PostSearchResultDTO> result =
                postSearchService.search(request, filter);

        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(0);
        assertThat(result.getDtoList()).isNotNull();
    }

    @Test
    void 제목만_검색된다() {
        PostSearchFilter filter = new PostSearchFilter();
        filter.setKeyword("과식");
        filter.setTitleChecked(true);
        filter.setContentChecked(false);
        filter.setCommentChecked(false);
        filter.setCategoryChecked(false);
        filter.setAuthorChecked(false);

        PageRequestDTO page = new PageRequestDTO(0, 10, "id", SortDirection.DESC);

        PostSearchRequest request = new PostSearchRequest(filter, page);

        PageResponseDTO<PostSearchResultDTO> result =
                postSearchService.search(request, filter);

        result.getDtoList().forEach(dto -> {System.out.println(dto);});

    }

}
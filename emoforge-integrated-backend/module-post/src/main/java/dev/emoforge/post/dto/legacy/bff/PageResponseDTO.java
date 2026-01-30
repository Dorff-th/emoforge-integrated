package dev.emoforge.post.dto.legacy.bff;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.query.PostListItemSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
/**
 * 페이징 응답을 표준화하기 위한 BFF 전용 DTO.
 *
 * PostController.getPostList(), PostController.getPostListByTag()에서 사용되며,
 * PageRequestDTO로 전달된 페이지 정보와 totalElements(전체 데이터 개수)를 기반으로
 * 페이징 UI를 구성하기 위한 모든 정보를 계산하여 제공한다.
 *
 * 🔎 사용되는 Controller API:
 * 1) PostController.getPostList()
 *    → GET /api/posts
 *
 * 2) PostController.getPostListByTag()
 *    → GET /api/posts/tags/{tagName}
 *
 * 주요 특징:
 * - BFF(Facade) 계층에서 계산된 페이징 정보를 포함
 * - startPage / endPage / prev / next 등 UI 렌더링에 필요한 정보 제공
 * - dtoList에는 페이지에 노출될 실제 데이터 목록(T 타입 리스트)이 포함됨
 *
 * 구성 필드:
 * - page: 현재 페이지 번호 (1부터 시작)
 * - size: 페이지 크기
 * - totalPages: 총 페이지 수
 * - totalElements: 전체 데이터 개수
 * - startPage, endPage: 화면에 보여줄 페이지 네비게이션 범위
 * - prev, next: 네비게이션 '이전/다음' 버튼 활성화 여부
 * - dtoList: 현재 페이지에 표시될 데이터 목록
 *
 * UI 구성 예:
 * 페이지단위가 10이고 현재 페이지가 7이면
 * - startPage = 1
 * - endPage = 10
 */
@Schema(
    description = """
                페이징 응답 표준 DTO (BFF).

                사용 API:
                - GET /api/posts
                - GET /api/posts/tags/{tagName}

                페이지네이션 UI 구성에 필요한 모든 계산된 값을 포함하며,
                실제 데이터 목록은 dtoList(T 제네릭 타입)에 담긴다.
                """
)
@Getter
public class PageResponseDTO<T> {

    @Schema(description = "현재 페이지 번호(1부터 시작)", example = "1")
    private final int page;

    @Schema(description = "페이지 크기", example = "10")
    private final int size;

    @Schema(description = "총 페이지 수", example = "5")
    private final int totalPages;

    @Schema(description = "전체 데이터 개수", example = "52")
    private final long totalElements;

    @Schema(description = "페이지 네비게이션 시작 번호", example = "1")
    private final int startPage;

    @Schema(description = "페이지 네비게이션 끝 번호", example = "10")
    private final int endPage;

    @Schema(description = "이전 페이지 블록 존재 여부", example = "false")
    private final boolean prev;

    @Schema(description = "다음 페이지 블록 존재 여부", example = "true")
    private final boolean next;

    @Schema(description = "현재 페이지의 실제 데이터 목록", example = "[...]")
    private final List<T> dtoList;

    public PageResponseDTO(PageRequestDTO requestDTO, long totalElements, List<T> dtoList, int pageCountToShow) {
        this.page = requestDTO.page();
        this.size = requestDTO.size();
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / this.size);
        this.dtoList = dtoList;

        int blockSize = Math.max(pageCountToShow, 1);
        int currentPage = requestDTO.page();
        int tempEnd = (int) (Math.ceil(currentPage / (double) blockSize) * blockSize);
        this.startPage = tempEnd - (blockSize - 1);
        this.endPage = Math.min(tempEnd, totalPages);

        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPages;
    }
}

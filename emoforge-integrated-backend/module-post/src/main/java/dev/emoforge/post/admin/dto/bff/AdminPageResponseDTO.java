package dev.emoforge.post.admin.dto.bff;

import dev.emoforge.post.dto.internal.PageRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminPageResponseDTO<T> {
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

    public AdminPageResponseDTO(PageRequestDTO requestDTO, long totalElements, List<T> dtoList, int pageCountToShow) {
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

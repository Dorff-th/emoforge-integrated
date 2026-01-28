package dev.emoforge.diary.dto.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<T> {

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private List<T> dtoList;

    public PageResponseDTO(PageRequestDTO requestDTO, long totalElements, List<T> dtoList, int pageCountToShow) {
        this.page = requestDTO.getPage();
        this.size = requestDTO.getSize();
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.dtoList = dtoList;

        // 페이지 블럭 계산
        int tempEnd = (int) (Math.ceil(this.page / (double) pageCountToShow) * pageCountToShow);
        this.startPage = tempEnd - (pageCountToShow - 1);
        this.endPage = Math.min(tempEnd, totalPages);
        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPages;
    }
}


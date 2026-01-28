package dev.emoforge.diary.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int page = 1; // 기본 1페이지

    @Builder.Default
    private int size = 10; // 기본 페이지 크기

    private String sort; // 정렬 기준 (예: createdAt)

    public int getOffset() {
        return (page - 1) * size;
    }
}

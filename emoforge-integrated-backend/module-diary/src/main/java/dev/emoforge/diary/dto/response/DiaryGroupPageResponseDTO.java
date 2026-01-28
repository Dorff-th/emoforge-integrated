package dev.emoforge.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DiaryGroupPageResponseDTO
 *
 * 회고 목록 조회(/api/diary/diaries) API의 페이징 응답 DTO.
 *
 * ✔ 날짜별로 그룹화된 회고 리스트(content)
 * ✔ 현재 페이지(currentPage)
 * ✔ 전체 페이지 수(totalPages)
 * ✔ 전체 데이터 수(totalElements)
 * ✔ 마지막 페이지 여부(isLast)
 *
 * DiaryEntryController.getDiaryList() 에서 사용되며,
 * React '회고 목록 화면'에 전달되는 최종 페이징 데이터 구조이다.
 */
@Schema(description = "회고 목록(날짜별 그룹) 페이징 응답 DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryGroupPageResponseDTO {

    @Schema(
            description = "날짜별로 그룹화된 회고 데이터 리스트",
            example = "[{ \"date\": \"2025-01-01\", \"entries\": [...] }]"
    )
    private List<DiaryGroupResponseDTO> content; // 날짜별 그룹 리스트

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int currentPage;

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPages;

    @Schema(description = "전체 데이터 개수", example = "42")
    private long totalElements;

    @Schema(description = "현재 페이지가 마지막 페이지인지 여부", example = "false")
    private boolean isLast;
}


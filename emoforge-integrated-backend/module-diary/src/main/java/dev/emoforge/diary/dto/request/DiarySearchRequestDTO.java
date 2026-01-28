package dev.emoforge.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DiarySearchRequestDTO
 *
 * 회고 통합 검색 요청 DTO.
 *
 * 사용 위치:
 *  - DiarySearchController.search()
 *
 * 검색 조건에는 다음 항목들이 포함될 수 있다:
 *
 *  ✔ query          : 검색 키워드 (본문/감정문/피드백 등)
 *  ✔ fields         : 어떤 필드를 검색할지 지정 (예: ["content", "feelingKo", "feelingEn"])
 *  ✔ memberUuid     : 검색을 요청한 사용자 UUID (Controller에서 자동 주입)
 *  ✔ emotionMap     : 감정 점수 범위 검색용 (예: {"min":1, "max":5})
 *  ✔ diaryDateMap   : 날짜 범위 검색용   (예: {"start":"2025-01-01", "end":"2025-01-31"})
 *
 * React 검색 페이지에서 다양한 필터를 조합해 보내는 구조이다.
 */
@Schema(description = "회고 통합검색 요청 DTO")
@Getter
@Setter
@ToString
public class DiarySearchRequestDTO {

    @Schema(
            description = "검색 키워드 (본문, 감정문, 영어문, 피드백 등 전반 검색)",
            example = "운동"
    )
    private String query;

    @Schema(
            description = """
                    어떤 필드를 검색할지 설정하는 리스트.
                    예: ["content", "feelingKo", "feelingEn", "feedback"]
                    """,
            example = "[\"content\", \"feelingKo\"]"
    )
    private List<String> fields;

    @Schema(
            description = "검색 요청자의 UUID (Controller에서 자동 설정됨)",
            example = "3b9c1d22-f213-4d33-b0aa-88c1c312a0e7"
    )
    private String memberUuid;

    @Schema(
            description = """
                    감정 점수 범위 검색용 Map.
                    key: "min", "max"
                    예: {"min": 2, "max": 5}
                    """,
            example = "{\"min\":2, \"max\":5}"
    )
    private Map<String, Integer> emotionMap;

    @Schema(
            description = """
                    날짜 범위 검색용 Map.
                    key: "start", "end"
                    
                    예:
                    {
                      "start": "2025-01-01",
                      "end": "2025-01-31"
                    }
                    """,
            example = "{\"start\":\"2025-01-01\", \"end\":\"2025-01-31\"}"
    )
    private Map<String, LocalDate> diaryDateMap;
}

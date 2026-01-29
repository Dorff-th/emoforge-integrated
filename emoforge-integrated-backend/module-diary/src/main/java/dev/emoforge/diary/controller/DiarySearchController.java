package dev.emoforge.diary.controller;


import dev.emoforge.diary.dto.request.DiarySearchRequestDTO;
import dev.emoforge.diary.dto.response.DiarySearchResultDTO;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.diary.service.DiarySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DiarySearchController
 *
 * 감정 & 회고 서비스의 통합 검색 기능 엔드포인트.
 *
 * 제공 기능:
 *  - 상단 헤더 검색창에서 검색 시 즉시 결과 반환
 *  - 검색결과 페이지에서 조건 변경 후 재검색
 *  - 날짜범위, 감정 점수, 키워드 등을 포함한 복합 검색 처리
 *  - 모든 검색 결과는 페이징(Page<DiarySearchResultDTO>) 형태로 반환
 *
 * 내부적으로 인증된 사용자 UUID를 기반으로
 * DiarySearchService에서 사용자별 검색 범위를 제한한다.
 */
@Tag(name = "DiarySearch", description = "회고 통합 검색 API")
@RestController
@RequestMapping("/api/diary/diaries/search")
@RequiredArgsConstructor
@Slf4j
public class DiarySearchController {

    private final DiarySearchService diarySearchService;


    // --------------------------------------------------------
    // 🔹 회고 통합 검색 (POST)
    // --------------------------------------------------------
    @Operation(
            summary = "회고 통합 검색",
            description = """
                    감정 & 회고 전체 영역을 대상으로 통합 검색을 수행합니다.
                    검색 조건에는 날짜 범위, 감정 점수, 키워드, 습관 태그 등이 포함될 수 있습니다.
                    모든 결과는 페이징(Page) 형태로 반환됩니다.
                    
                    사용 위치:
                    - 상단 헤더 검색창 입력 → 즉시 결과 출력
                    - 검색결과 페이지에서 검색조건 입력 후 검색 버튼 클릭
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공 (페이징 결과 반환)"),
            @ApiResponse(responseCode = "400", description = "검색 조건 파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping
    public Page<DiarySearchResultDTO> search(Authentication authentication,
                                             @RequestBody DiarySearchRequestDTO request, Pageable pageable) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        request.setMemberUuid(memberUuid);

        log.info(request.toString());

        return diarySearchService.search(memberUuid, request, pageable);
    }
}

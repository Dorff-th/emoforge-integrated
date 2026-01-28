package dev.emoforge.diary.controller;


import dev.emoforge.diary.dto.response.GPTSummaryResponseDTO;
import dev.emoforge.diary.dto.response.SummaryResponseDTO;
import dev.emoforge.diary.global.security.CustomUserPrincipal;
import dev.emoforge.diary.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SummaryController
 *
 * 감정 & 회고 애플리케이션의 "홈 화면 요약" 데이터를 제공하는 컨트롤러.
 *
 * 제공 기능:
 *
 * 1) 오늘의 감정 요약 조회 (/today)
 *    - 감정 점수
 *    - 한글 감정 문장(feelingKo)
 *    - 영어 감정 문장(feelingEn)
 *    - 오늘 실천한 습관 리스트
 *    - 오늘 작성한 회고 내용(최신 1개)
 *    - GPT 피드백
 *    - GPT 요약 (있을 경우)
 *
 * 2) 오늘의 GPT 요약만 단독 조회 (/gpt/today)
 *    - GPT가 오늘 날짜 기준으로 생성한 요약 데이터만 조회
 *
 * 이 API는 로그인 직후 보여지는 "오늘의 요약(Home Summary)" 화면의 데이터 소스다.
 */
@Tag(name = "Summary", description = "홈 화면용 감정·회고 요약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary/summary")
@Slf4j
public class SummaryController {

    private final SummaryService summaryService;

    //메뉴 : 로그인 하면 바로나오는 첫화면 (개발 의도는 오늘하루 감정 요약인데 오늘 감정 최신 1개만 나오도록 변경)
    // --------------------------------------------------------
    // 🔹 오늘 하루 요약 조회 (홈 첫 화면)
    // --------------------------------------------------------
    @Operation(
            summary = "오늘의 감정·회고 요약 조회",
            description = """
                    앱 홈 화면에 출력되는 오늘의 감정/습관/회고/GPT 피드백/요약 데이터를 조회합니다.
                    가장 최근 작성된 DiaryEntry를 기준으로 요약 정보를 구성합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "오늘의 요약 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "오늘 날짜의 회고 데이터 없음")
    })
    @GetMapping("/today")
    public ResponseEntity<SummaryResponseDTO> getTodaySummary(Authentication authentication) {

        //String memberUuid = authentication.getPrincipal().toString();
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        log.info("✅ SummaryService 진입: memberUuid={}", memberUuid);

        log.debug(memberUuid);

        try {
            SummaryResponseDTO entry =  summaryService.getTodaySummary(memberUuid);
            return ResponseEntity.ok(entry);
        } catch (Exception e) {
            log.error("❌ SummaryService 예외 발생: {}", e.getMessage(), e);
            throw e;
        }

    }


    //메뉴 : 로그인 하면 바로나오는 첫화면의 오늘의 GPT 요약 조회 (gpt_summary)
    // --------------------------------------------------------
    // 🔹 오늘의 GPT 요약만 조회
    // --------------------------------------------------------
    @Operation(
            summary = "오늘의 GPT 요약 조회",
            description = """
                    오늘 날짜 기준으로 저장된 GPT 요약(gpt_summary)을 조회합니다.
                    GPT 요약만 필요할 때 사용하는 경량 API입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "GPT 요약 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "GPT 요약 없음")
    })
    @GetMapping("/gpt/today")
    public ResponseEntity<GPTSummaryResponseDTO> getTodayGptSummary(Authentication authentication) {

        //String memberUuid = authentication.getPrincipal().toString();
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        var result = summaryService.getTodayGPTSummary(memberUuid);
        return ResponseEntity.ok(result);
    }
}

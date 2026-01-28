package dev.emoforge.diary.controller;


import dev.emoforge.diary.dto.statistics.EmotionStatisticsDTO;
import dev.emoforge.diary.global.security.CustomUserPrincipal;
import dev.emoforge.diary.service.EmotionStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * EmotionStatisticsController
 *
 * 감정점수 기반 통계 데이터를 제공하는 컨트롤러.
 *
 * 제공 기능:
 *  - 특정 기간(startDate ~ endDate) 동안의 감정 점수 변화/분포 통계를 조회
 *
 * 사용 위치:
 *  - /api/diary/statistics/emotion
 *
 * 특징:
 *  - 인증된 사용자(UUID) 기준으로 개인별 감정 기록만 집계
 *  - 프론트에서 주간/월간 통계 대시보드 구성 시 사용
 */
@Tag(name = "EmotionStatistics", description = "감정 점수 통계 API")
@RestController
@RequestMapping("/api/diary/statistics/emotion")
@RequiredArgsConstructor
public class EmotionStatisticsController {

    private final EmotionStatisticsService emotionStatisticsService;

    // --------------------------------------------------------
    // 🔹 감정 통계 조회
    // --------------------------------------------------------
    @Operation(
            summary = "감정 통계 조회",
            description = """
                    특정 기간(startDate ~ endDate) 동안의 감정 점수 통계를 조회합니다.
                    조회된 통계는 감정 흐름, 평균 감정점수, 감정 분포 등 프론트 대시보드 구성에 사용됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "통계 조회 성공"),
            @ApiResponse(responseCode = "400", description = "날짜 파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    @GetMapping
    public EmotionStatisticsDTO getEmotionStatistics(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication

    ) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        return emotionStatisticsService.getEmotionStatistics(memberUuid, startDate, endDate);
    }
}

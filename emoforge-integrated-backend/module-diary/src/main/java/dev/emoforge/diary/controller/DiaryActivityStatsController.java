package dev.emoforge.diary.controller;

import dev.emoforge.diary.dto.response.MemberDiaryStatsResponse;
import dev.emoforge.diary.global.security.CustomUserPrincipal;
import dev.emoforge.diary.service.DiaryActivityStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자의 감정·회고 활동량을 집계하여 제공하는 컨트롤러.
 *
 * 이 API는 로그인한 사용자가 작성한 일기(회고) 데이터를 기반으로,
 * 감정 점수, 작성 횟수 등 간단한 활동 통계를 조회하는 용도로 사용됩니다.
 *
 * ✔ Authentication 기반 사용자 식별
 * ✔ 사용자 개별 활동량 요약 제공
 * ✔ Summary 화면 / 마이페이지 대시보드에서 활용
 *
 * 주요 사용 예:
 * - “오늘의 요약” 첫 화면
 * - 마이페이지 활동량 그래프·카드 UI
 * - 자기분석 통계 기능
 */
@Tag(
        name = "Diary Activity Statistics API",
        description = "사용자의 감정·회고 활동량을 간단한 통계 형태로 제공하는 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
@Slf4j
public class DiaryActivityStatsController {

    private final DiaryActivityStatsService statsService;

    @Operation(
            summary = "내 감정/회고 활동 통계 조회",
            description = """
                    로그인한 사용자가 지금까지 작성한 감정·회고 데이터의 활동 통계를 조회합니다.
                    
                    ✔ Authentication → memberUuid 기반 조회  
                    ✔ Diary 테이블 기반 집계  
                    ✔ 감정 평균, 총 작성 횟수 등 간단한 통계 제공
                    
                    반환 예:
                    {
                      "totalDiaryCount": 42,
                      "averageEmotion": 65,
                      "latestEmotion": 70
                    }
                    """
    )
    @GetMapping("/me/statistics")
    public ResponseEntity<MemberDiaryStatsResponse> getMyDiaryStats(
            Authentication authentication
    ) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        log.info("📊 Diary activity stats 요청: memberUuid={}", memberUuid);

        MemberDiaryStatsResponse response =
                statsService.getUserDiaryStats(memberUuid);

        return ResponseEntity.ok(response);
    }
}
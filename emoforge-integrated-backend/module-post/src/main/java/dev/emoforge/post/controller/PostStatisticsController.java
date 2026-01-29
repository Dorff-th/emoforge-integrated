package dev.emoforge.post.controller;

import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.post.dto.internal.PostStatsResponse;
import dev.emoforge.post.service.internal.PostStatisticsService;
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
 * 사용자의 게시글/댓글 활동 통계를 조회하는 컨트롤러.
 *
 * 이 API는 로그인한 사용자가 지금까지 작성한 게시글 수와
 * 댓글 수를 빠르게 확인할 수 있도록 제공하는 ‘마이페이지 전용 통계 API’입니다.
 *
 * ✔ 로그인 사용자만 호출 가능
 * ✔ Authentication → CustomUserPrincipal 기반으로 memberUuid 추출
 * ✔ 단순하고 빠른 통계 조회 (Post + Comment 개수 반환)
 *
 * 주요 사용 예:
 * - 사용자 프로필 화면
 * - 마이페이지 대시보드
 * - 활동량 요약 위젯
 */
@Tag(
    name = "Post Statistics API",
    description = "사용자 자신의 게시글/댓글 통계를 조회하는 API"
)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostStatisticsController {

    private final PostStatisticsService postStatisticsService;

    @Operation(
        summary = "내 게시글/댓글 통계 조회",
        description = """
                    로그인한 사용자가 작성한 게시글 수와 댓글 수를 조회합니다.

                    ✔ Authentication 기반 사용자 식별
                    ✔ 게시글 수 + 댓글 수 2가지 지표 반환
                    ✔ 마이페이지·프로필 화면에서 사용

                    반환 예:
                    {
                      "postCount": 12,
                      "commentCount": 34
                    }
                    """
    )
    @GetMapping("/me/statistics")
    public ResponseEntity<PostStatsResponse> memberPostStatistics(Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();

        PostStatsResponse response = postStatisticsService.getPostStatistics(memberUuid);

        return ResponseEntity.ok(response);
    }
}

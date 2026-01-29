package dev.emoforge.diary.controller;

import dev.emoforge.diary.dto.music.MusicRecommendHistoryDTO;
import dev.emoforge.diary.dto.music.MusicRecommendRequest;
import dev.emoforge.diary.dto.music.RecommendResultDTO;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.diary.service.MusicRecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * MusicRecommendController
 *
 * 감정 기반 음악 추천 기능을 제공하는 컨트롤러.
 *
 * 기능 요약:
 *
 * 1) 추천된 음악 히스토리 조회
 *    - DiaryEntry에 대해 과거에 생성된 음악 추천 리스트를 반환
 *    - YouTube 영상 링크 / 제목 / 아티스트 / 감정 기반 추천 사유 등이 포함
 *
 * 2) 감정 데이터 기반 음악 추천 생성
 *    - LangGraph-Service 호출
 *    - DiaryEntry의 감정 정보(emotion, feelingKo, feelingEn 등)를 기반으로
 *      YouTube 음악을 추천하고 DB에 저장
 *
 * 사용 위치:
 *  - /api/diary/music/{diaryEntryId}/recommendations  → 기존 추천 목록 조회
 *  - /api/diary/music/recommend                      → AI 기반 신규 추천 생성
 */
@Tag(name = "MusicRecommendation", description = "감정 기반 음악 추천 API")
@RestController
@RequestMapping("/api/diary/music")
@RequiredArgsConstructor
@Slf4j
public class MusicRecommendController {

    private final MusicRecommendService musicRecommendService;

    // --------------------------------------------------------
    // 🔹 추천된 음악 목록 조회 (히스토리)
    // --------------------------------------------------------
    @Operation(
            summary = "추천된 음악 목록 조회",
            description = """
                    특정 회고(DiaryEntry)에 대해 과거에 생성된 음악 추천 목록을 반환합니다.
                    LangGraph-Service를 거쳐 생성된 YouTube 음악 추천 리스트가 저장되어 있으며,
                    해당 목록을 그대로 조회하는 용도의 API입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 음악 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 DiaryEntry에 대한 추천 기록 없음")
    })
    @GetMapping("/{diaryEntryId}/recommendations")
        public ResponseEntity<MusicRecommendHistoryDTO> getRecommendations(@PathVariable("diaryEntryId") Long diaryEntryId) {
        MusicRecommendHistoryDTO dto = musicRecommendService.getRecommendationsForDiary(diaryEntryId);
        return ResponseEntity.ok(dto);
    }

    // --------------------------------------------------------
    // 🔹 감정 기반 음악 추천 생성
    // --------------------------------------------------------
    @Operation(
            summary = "감정 기반 음악 추천 생성",
            description = """
                LangGraph-Service를 호출하여 DiaryEntry의 감정 데이터를 기반으로 음악을 추천합니다.
                
                내부 동작:
                1. DiaryEntry의 감정 점수, feelingKo/feelingEn, GPT 기반 피드백 등을 분석
                2. LangGraph-Service로 전달하여 감정 기반 음악 추천 생성
                3. 추천된 YouTube 음악 정보(영상 링크/제목 등)를 DB에 저장
                4. 응답으로 RecommendResultDTO 반환

                사용 예:
                - "슬픔 감정일 때 감정을 안정시키는 음악 추천"
                - "피곤함/지침 기반으로 릴랙싱 음악 추천"
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 추천 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "DiaryEntry를 찾을 수 없음")
    })
    @PostMapping("/recommend")
    public ResponseEntity<RecommendResultDTO> recommendMusic(
            @RequestBody MusicRecommendRequest request,
            Authentication authentication
    ) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        RecommendResultDTO result = musicRecommendService.recommendForDiaryEntry(
                request.getDiaryEntryId(),
                request.getArtistPreferences(),
                memberUuid
        );
        return ResponseEntity.ok(result);
    }
}

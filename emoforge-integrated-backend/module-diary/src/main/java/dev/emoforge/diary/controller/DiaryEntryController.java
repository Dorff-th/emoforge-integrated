package dev.emoforge.diary.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import dev.emoforge.diary.domain.DiaryEntry;
import dev.emoforge.diary.dto.request.DiarySaveRequestDTO;
import dev.emoforge.diary.dto.request.GPTSummaryRequestDTO;
import dev.emoforge.diary.dto.response.DiaryGroupPageResponseDTO;
import dev.emoforge.diary.dto.response.DiaryGroupResponseDTO;
import dev.emoforge.diary.dto.response.GPTSummaryResponseDTO;
import dev.emoforge.diary.global.security.CustomUserPrincipal;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.GptSummaryRepository;
import dev.emoforge.diary.service.DiaryEntryService;
import dev.emoforge.diary.service.GptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DiaryEntryController
 *
 * 감정 & 회고 다이어리 기능의 메인 엔드포인트 제공.
 * 제공 기능 목록:
 *  - 회고 목록 조회 (페이징 + 날짜 그룹)
 *  - 월간 회고 달력 조회
 *  - 회고 작성
 *  - GPT 회고 요약 생성
 *  - 회고 1건 삭제 (옵션: GPT 요약 포함 삭제)
 *  - GPT 요약만 단독 삭제
 *  - 특정 날짜의 모든 회고 + GPT 요약 삭제
 *
 * 내부적으로 Auth-Service 인증에서 전달된 memberUuid 기반으로
 * Diary-Service의 CRUD 및 GPT 연동 로직을 처리한다.
 *
 * BFF나 React Frontend에서는 각각:
 *  - 회고 리스트: /api/diary/diaries
 *  - 달력 데이터: /api/diary/diaries/monthly?yearMonth=2025-01-01
 *  - 회고 저장: POST /api/diary/diaries
 *  - GPT 요약 생성: POST /api/diary/gpt-summary
 *  - 회고 삭제: DELETE /api/diary/{id}
 *  - GPT 요약 삭제: DELETE /api/diary/summary
 *  - 특정 날짜 전체 삭제: DELETE /api/diary/all
 */
@Tag(name = "DiaryEntry", description = "회고(다이어리) 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
@Slf4j
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;
    private final GptService gptService;
    private final DiaryEntryRepository diaryEntryRepository;
    private final GptSummaryRepository gptSummaryRepository;

    // --------------------------------------------------------
    // 🔹 회고 목록 조회 (페이징)
    // --------------------------------------------------------
    @Operation(
            summary = "회고 목록 조회 (최신순 + 날짜별 그룹)",
            description = """
                    사용자의 회고 데이터를 날짜별로 그룹화하여 페이징 형태로 조회합니다.
                    React 프론트엔드의 '회고 목록' 화면이 호출하는 주요 API입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회고 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/diaries")
    public DiaryGroupPageResponseDTO getDiaryList(
            Authentication authentication,
            Pageable pageable
    ) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        return diaryEntryService.getDiaryListGroupedByDate(memberUuid, pageable);
    }


    // --------------------------------------------------------
    // 🔹 월간 회고 달력 조회
    // --------------------------------------------------------
    @Operation(
            summary = "월간 회고 달력 조회",
            description = """
                    특정 연-월(예: 2025-01-01)을 기준으로 해당 월에 작성된 회고 데이터를
                    날짜별로 그룹화하여 반환합니다.
                    달력 화면에서 특정 날짜에 기록이 있는지 체크하는 용도로 사용됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월간 회고 데이터 조회 성공"),
            @ApiResponse(responseCode = "400", description = "yearMonth 파라미터 형식 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/diaries/monthly")
    public List<DiaryGroupResponseDTO> getDiaryListMonthly(Authentication authentication,
                                                           @RequestParam("yearMonth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate yearMonth) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();

        return diaryEntryService.getEntriesForMonthlyGroupedByDate(memberUuid, yearMonth);

    }

    // --------------------------------------------------------
    // 🔹 회고 작성
    // --------------------------------------------------------
    @Operation(
            summary = "회고 작성",
            description = """
                    하루의 감정점수, 습관 체크, 일기 내용 등을 저장합니다.
                    저장 후에는 별도의 GPT 요약은 자동 생성되지 않으며,
                    사용자가 날짜 선택 화면에서 GPT 요약 버튼을 눌러야 생성됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회고 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 형식 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/diaries")
    public ResponseEntity<Void> saveDiary(
            Authentication authentication,
            @RequestBody DiarySaveRequestDTO dto
    ) throws JsonProcessingException {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();
        diaryEntryService.saveDiary(memberUuid, dto);
        return ResponseEntity.ok().build();
    }

    // --------------------------------------------------------
    // 🔹 GPT 요약 생성
    // --------------------------------------------------------
    @Operation(
            summary = "GPT 회고 요약 생성",
            description = """
                    특정 날짜에 대해 GPT 기반 회고 요약을 생성하고 DB에 저장합니다.
                    이미 요약이 존재하면 새로운 요약으로 덮어쓰지는 않습니다.
                    프론트에서는 요약이 없을 때만 버튼을 노출합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "GPT 요약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "날짜 파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/gpt-summary")
    public ResponseEntity<GPTSummaryResponseDTO> generateSummary(
            Authentication authentication,
            @RequestBody GPTSummaryRequestDTO request
    ) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();

        String summary = gptService.generateAndSaveSummary(
                memberUuid,
                request.getDate()
        );

        return ResponseEntity.ok(new GPTSummaryResponseDTO(summary));
    }

    // --------------------------------------------------------
    // 🔹 회고 1건 삭제
    // --------------------------------------------------------
    @Operation(
            summary = "회고 1건 삭제",
            description = """
                    회고 ID 기준으로 단일 회고를 삭제합니다.
                    withSummary=true 인 경우 해당 날짜의 GPT 요약도 함께 삭제됩니다.
                    삭제 시 작성자 본인 여부를 서버에서 검증합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회고 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "회고를 찾을 수 없음")
    })
    @DeleteMapping("/{diaryEntryId}")
    public ResponseEntity<?> deleteDiaryEntry(
            @PathVariable("diaryEntryId") Long diaryEntryId,
            @RequestParam(name = "withSummary", defaultValue = "false") boolean withSummary,
            Authentication authentication) {

        // 1️⃣ 인증 정보에서 uuid 추출
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();


        String currentUuid = principal.getUuid();

        // 2️⃣ DB에서 회고 조회
        DiaryEntry target = diaryEntryRepository.findById(diaryEntryId)
                .orElseThrow(() -> new IllegalArgumentException("회고를 찾을 수 없습니다."));

        // 3️⃣ 현재 사용자와 회고 작성자 일치 여부 확인
        if (!target.getMemberUuid().equals(currentUuid)) {
            log.warn("❌ 삭제 권한 없음: memberUuid={}, currentUuid={}",
                    target.getMemberUuid(), currentUuid);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("삭제 권한이 없습니다.");
        }

        log.debug("✅ 회고(ID={}) 삭제 완료 by {}", diaryEntryId, currentUuid);


        diaryEntryService.deleteDiaryEntry(diaryEntryId, withSummary);

        return ResponseEntity.ok().body(
                String.format("회고(ID=%d)가 삭제되었습니다. (GPT 요약 삭제 여부: %s)",
                        diaryEntryId, withSummary ? "포함" : "미포함")
        );
    }

    // --------------------------------------------------------
    // 🔹 GPT 요약만 삭제
    // --------------------------------------------------------
    @Operation(
            summary = "GPT 요약만 삭제",
            description = "특정 날짜에 대한 GPT 요약 데이터를 단독으로 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "GPT 요약 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "날짜 파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @DeleteMapping("/summary")
    public ResponseEntity<?> deleteSummaryOnly(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {

        log.debug("\n\n\n====/summary 실행");

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String currentUuid = principal.getUuid();
        gptSummaryRepository.deleteByMemberUuidAndDiaryDate(currentUuid, date);

        return ResponseEntity.ok().body(
                String.format("회원 %s의 %s GPT 요약이 삭제되었습니다.", currentUuid, date)
        );
    }

    // --------------------------------------------------------
    // 🔹 특정 날짜의 회고 전체 삭제
    // --------------------------------------------------------
    @Operation(
            summary = "특정 날짜의 회고 전체 삭제",
            description = """
                    yyyy-MM-dd 형식 날짜 기준으로
                    해당 사용자의 모든 회고 + GPT 요약을 삭제합니다.
                    캘린더 화면에서 '해당 날짜 전체 삭제' 기능에 매핑됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 날짜 회고 전체 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "날짜 파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllByDate(
            @RequestParam("date") LocalDate date,
            Authentication authentication) {

        // 1️⃣ 인증된 사용자 UUID 추출
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();


        String currentUuid = principal.getUuid();

        // 2️⃣ 그 사용자의 해당 날짜 회고 전체 삭제
        diaryEntryService.deleteAllByDate(currentUuid, date);

        // 3️⃣ 로깅
        log.info("✅ 회원 {}의 {} 회고 및 GPT 요약이 모두 삭제되었습니다.", currentUuid, date);

        // 4️⃣ 응답 반환
        return ResponseEntity.ok().body(
                String.format("회원 %s의 %s 회고 및 GPT 요약이 모두 삭제되었습니다.", currentUuid, date)
        );
    }

}

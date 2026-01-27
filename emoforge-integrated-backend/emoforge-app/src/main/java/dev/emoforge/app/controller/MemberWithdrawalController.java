package dev.emoforge.app.controller;

import dev.emoforge.core.security.CustomUserPrincipal;
import dev.emoforge.auth.service.MemberWithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/me")
@Slf4j
@Tag(name = "Member Withdrawal", description = "회원 탈퇴 신청/취소 API")
public class MemberWithdrawalController {

    private final MemberWithdrawalService withdrawalService;

    /**
     * 🔥 회원 탈퇴 신청
     * - deleted = true
     * - deleted_at = now()
     * - 호출 후 프론트는 자동 로그아웃 처리
     */
    @PostMapping("/withdrawal")
    @Operation(
            summary = "회원 탈퇴 신청",
            description = "회원 탈퇴를 요청하며, 계정은 즉시 비활성 처리되고 10일 뒤 완전 삭제 대상이 됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 신청 완료"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal CustomUserPrincipal user) {

        String memberUuid = user.getUuid();
        log.info("🚪 탈퇴 요청 API 호출: uuid={}", memberUuid);

        withdrawalService.requestWithdrawal(memberUuid);

        return ResponseEntity.ok("회원 탈퇴 신청이 완료되었습니다.");
    }

    /**
     * 🔥 회원 탈퇴 취소
     * - deleted = false
     * - deleted_at = null
     * - 프론트는 이후 자동 로그인 처리 가능
     */
    @PostMapping("/withdrawal/cancel")
    @Operation(
            summary = "회원 탈퇴 취소",
            description = "탈퇴 대기 상태를 취소하고 계정을 다시 활성화합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 취소 완료"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> cancelWithdrawal(@AuthenticationPrincipal CustomUserPrincipal user) {

        String memberUuid = user.getUuid();
        log.info("↩️ 탈퇴 취소 요청: uuid={}", memberUuid);

        withdrawalService.cancelWithdrawal(memberUuid);

        return ResponseEntity.ok("회원 탈퇴가 취소되었습니다.");
    }


}

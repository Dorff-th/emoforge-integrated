package dev.emoforge.app.controller.admin;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.service.admin.MemberAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MemberAdminController
 *
 * 관리자 전용 "회원 관리" API 컨트롤러.
 * - 모든 회원 목록 조회
 * - 회원 상태 변경 (ACTIVE / INACTIVE)
 * - 회원 탈퇴 플래그(soft delete) 변경
 *
 * 실제 데이터를 삭제하는 API가 아니라,
 * 서비스 운영을 위한 관리용 정보(status, deleted flag)를 조작하는 용도이다.
 */
@Tag(name = "AdminMember", description = "관리자 전용 회원 관리 API (Admin Only)")
@RestController
@RequestMapping("/api/auth/admin/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    // ---------------------------------------------------------
    // 🔹 회원 목록 조회
    // ---------------------------------------------------------
    @Operation(
            summary = "전체 회원 목록 조회 (관리자 전용)",
            description = """
                    모든 회원의 정보를 조회합니다. (관리자만 접근 가능)
                    이 API는 운영 관리, 사용자 모니터링, 관리자 대시보드에서 사용됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberAdminService.getAllMembers());
    }

    // ---------------------------------------------------------
    // 🔹 회원 상태 변경 (ACTIVE / INACTIVE)
    // ---------------------------------------------------------
    @Operation(
            summary = "회원 상태 변경 (관리자 전용)",
            description = """
                    특정 회원의 상태(Status)를 변경합니다.
                    - ACTIVE: 정상 이용 가능
                    - INACTIVE: 비활성화 (로그인 불가)
                    실제 데이터 삭제가 아닌 계정 잠금 처리 또는 운영상 관리 용도로 사용됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없음")
    })
    @PatchMapping("/{uuid}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable("uuid") String uuid,
            @RequestParam("status") String status
    ) {
        memberAdminService.updateStatus(uuid, MemberStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // 🔹 회원 탈퇴 여부 변경 (Soft Delete Flag)
    // ---------------------------------------------------------
    @Operation(
            summary = "회원 탈퇴 플래그 변경 (관리자 전용)",
            description = """
                    특정 회원의 '탈퇴 여부(deleted)' 플래그를 변경합니다.
                    - 실제 데이터를 삭제하는 것이 아니라,
                      운영 관리용 soft-delete 필드만 조정합니다.
                    - deleted = true → 탈퇴 처리된 회원
                    - deleted = false → 탈퇴 취소
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "플래그 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없음")
    })
    @PatchMapping("/{uuid}/deleted")
    public ResponseEntity<Member> updateDeleted(
            @PathVariable("uuid") String uuid,
            @RequestParam("deleted") boolean deleted
    ) {
        Member updated = memberAdminService.updateDeleted(uuid, deleted);
        return ResponseEntity.ok(updated); // ✅ 변경된 회원 반환
    }
}

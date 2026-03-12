package dev.emoforge.auth.controller.admin;

import dev.emoforge.auth.dto.admin.MemberSearchCondition;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.service.admin.AdminMemberService;
import dev.emoforge.auth.service.admin.MemberDeletionService;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * AdminMemberController
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
public class AdminMemberController {

    private final AdminMemberService adminMemberService;
    private final MemberDeletionService memberDeletionService;

    private final MemberRepository memberRepository;

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
    /*@GetMapping
    public ResponseEntity<Page<Member>> getAllMembers(Pageable pageable) {
        return ResponseEntity.ok(adminMemberService.getAllMembers(pageable));
    }*/
    @GetMapping
    public ResponseEntity<?> getAllMembers(PageRequestDTO requestDTO, MemberSearchCondition searchCondition) {
        return ResponseEntity.ok(adminMemberService.getMemberList(requestDTO, searchCondition));
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
        adminMemberService.updateStatus(uuid, MemberStatus.valueOf(status.toUpperCase()));
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
        Member updated = adminMemberService.updateDeleted(uuid, deleted);
        return ResponseEntity.ok(updated); // ✅ 변경된 회원 반환
    }

    // 2026-03-11: Added admin endpoint for full member deletion with audit actor tracking.
    @Operation(summary = "회원 완전 삭제", description = "관리자가 회원과 연관된 게시글, 다이어리, 프로필 이미지, 인증 데이터를 함께 삭제하고 감사 로그를 남깁니다.")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteMember(@PathVariable("uuid") UUID uuid,
                                             @AuthenticationPrincipal CustomUserPrincipal adminUser) {

        //백엔드에서도 탈퇴 상태(deleted = 1)인 사용자만 체크해서 완전 삭제 처리
        Member member = memberRepository.findByUuid(uuid.toString()).orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다!"));

        if (!member.isDeleted()) {
            throw new IllegalStateException("탈퇴된 회원만 삭제할 수 있습니다.");
        }
        memberDeletionService.deleteMember(uuid, UUID.fromString(adminUser.getUuid()));

        return ResponseEntity.noContent().build();
    }
}

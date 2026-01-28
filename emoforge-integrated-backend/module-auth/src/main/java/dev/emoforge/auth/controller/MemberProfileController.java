package dev.emoforge.auth.controller;

import dev.emoforge.auth.dto.AvailabilityResponse;
import dev.emoforge.auth.dto.MemberProfileResponse;
import dev.emoforge.auth.dto.UpdateEmailRequest;
import dev.emoforge.auth.dto.UpdateNicknameRequest;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.auth.service.MemberProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * MemberProfileController
 *
 * 회원 프로필 관리 기능을 담당하는 컨트롤러.
 * - 닉네임 중복 여부 확인
 * - 이메일 중복 여부 확인
 * - 닉네임 변경 요청 처리
 * - 이메일 변경 요청 처리
 *
 * 프로필 수정 시 필요한 검증 + 변경 로직의 진입점 역할을 한다.
 */
@Tag(name = "MemberProfile", description = "회원 프로필 관리 API")
@RestController
@RequestMapping("/api/auth/members")
@RequiredArgsConstructor
@Slf4j
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    // --------------------------------------------------------
    // 🔹 닉네임 중복 체크
    // --------------------------------------------------------
    @Operation(
            summary = "닉네임 중복 체크",
            description = "사용자가 입력한 닉네임이 이미 존재하는지 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 사용 가능"),
            @ApiResponse(responseCode = "409", description = "닉네임 중복됨")
    })
    @GetMapping("/check-nickname")
    public AvailabilityResponse checkNickname(@RequestParam("nickname") String nickname) {
        return memberProfileService.checkNickname(nickname);
    }

    // --------------------------------------------------------
    // 🔹 이메일 중복 체크
    // --------------------------------------------------------
    @Operation(
            summary = "이메일 중복 체크",
            description = "사용자가 변경하려는 이메일이 이미 존재하는지 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 사용 가능"),
            @ApiResponse(responseCode = "409", description = "이메일 중복됨")
    })
    @GetMapping("/check-email")
    public AvailabilityResponse checkEmail(@RequestParam("email") String email) {
        return memberProfileService.checkEmail(email);
    }

    // --------------------------------------------------------
    // 🔹 닉네임 변경
    // --------------------------------------------------------
    @Operation(
            summary = "닉네임 변경",
            description = "로그인한 사용자의 닉네임을 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "400", description = "변경할 닉네임이 유효하지 않음"),
            @ApiResponse(responseCode = "409", description = "닉네임 중복됨")
    })
    @PutMapping("/nickname")
    public MemberProfileResponse updateNickname(@RequestBody UpdateNicknameRequest req, @AuthenticationPrincipal CustomUserPrincipal user) {
        String uuid = user.getUuid();
        return memberProfileService.updateNickname(uuid, req.nickname());
    }

    // --------------------------------------------------------
    // 🔹 이메일 변경
    // --------------------------------------------------------
    @Operation(
            summary = "이메일 변경",
            description = "로그인한 사용자의 이메일을 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 변경 성공"),
            @ApiResponse(responseCode = "400", description = "변경할 이메일이 유효하지 않음"),
            @ApiResponse(responseCode = "409", description = "이메일 중복됨")
    })
    @PutMapping("/email")
    public MemberProfileResponse updateEmail(@RequestBody UpdateEmailRequest req, @AuthenticationPrincipal CustomUserPrincipal user) {
        String uuid = user.getUuid();
        return memberProfileService.updateEmail(uuid, req.email());
    }

    // --------------------------------------------------------
    // 🔹 BFF(Post-Service)에서 사용하는 회원 프로필 조회
    // --------------------------------------------------------
    @Operation(
            summary = "회원 프로필 조회 (BFF용)",
            description = "UUID를 기반으로 회원 프로필 정보를 조회합니다. Post-Service의 BFF(authClient)에서 사용합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없음")
    })
    @GetMapping("/{uuid}/profile")
    public MemberProfileResponse getProfile(@PathVariable("uuid") String uuid) {

        return memberProfileService.getProfile(uuid);
    }
}

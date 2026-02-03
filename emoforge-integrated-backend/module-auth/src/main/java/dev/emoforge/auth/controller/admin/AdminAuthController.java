package dev.emoforge.auth.controller.admin;

import dev.emoforge.auth.dto.admin.AdminLoginRequest;
import dev.emoforge.auth.dto.admin.AdminLoginResponse;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.service.LoginTokenService;
import dev.emoforge.core.properties.CookieProvider;
import dev.emoforge.auth.service.admin.AdminAuthService;
import dev.emoforge.auth.service.admin.RecaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



/**
 * AdminAuthController
 *
 * 관리자 인증 전용 컨트롤러.
 * - 일반 사용자 인증 API와 분리된 관리자용 로그인/로그아웃/인증 체크 기능을 제공한다.
 * - 관리자 인증은 admin_token(HttpOnly 쿠키) 기반으로 처리된다.
 * - reCAPTCHA 기반 자동화 방지 검증을 수행한다.
 *
 * 모든 엔드포인트는 운영상 민감하며, 관리자 페이지(admin-frontend)에서만 사용된다.
 */
@Tag(name = "AdminAuth", description = "관리자 전용 인증 API (일반 사용자 접근 불가)")
@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final RecaptchaService recaptchaService;
    private final LoginTokenService loginTokenService;
    //private final CookieProvider cookieProvider;
    private final MemberRepository memberRepository;

    // ---------------------------------------------------------
    // 🔹 관리자 로그인
    // ---------------------------------------------------------
    @Operation(
            summary = "관리자 로그인",
            description = """
                    관리자 계정 로그인 API입니다. (관리자 전용)
                    1. reCAPTCHA 검증 수행
                    2. 관리자 로그인 시도
                    3. admin_token(HttpOnly 쿠키) 발급
                    - 일반 사용자 로그인 API와 절대 혼용되지 않습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "403", description = "reCAPTCHA 검증 실패"),
            @ApiResponse(responseCode = "401", description = "잘못된 관리자 계정 정보")
    })
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletResponse response
    ) {
        if (!recaptchaService.verify(request.captchaToken())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AdminLoginResponse("reCAPTCHA 검증 실패"));
        }

        Member admin = adminAuthService.authenticate(request);

        loginTokenService.handleLoginSuccess(
                response,
                admin,
                LoginType.ADMIN
        );

        return ResponseEntity.ok(
                new AdminLoginResponse("관리자 로그인 성공")
        );
    }

    // ---------------------------------------------------------
    // 🔹 관리자 로그아웃
    // ---------------------------------------------------------
    @Operation(
            summary = "관리자 로그아웃",
            description = """
                    관리자 인증 쿠키(admin_token)를 삭제하여 로그아웃 처리합니다. (관리자 전용)
                    쿠키는 동일한 domain/path/sameSite/secure 옵션으로 제거해야 정상적으로 삭제됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 완료")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        loginTokenService.handleLogout(response, LoginType.ADMIN);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAdminInfo(Authentication authentication) {


        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("message", "인증되지 않았습니다."));
        }

        String memberUuid = authentication.getName();   // CustomUserPrincipal에셔 getUsername() return 을 uuid로 함
        Member member = memberRepository.findByUuid(memberUuid)
                .orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        String nickname = member.getNickname();
        String role = authentication.getAuthorities().iterator().next().getAuthority();


        return ResponseEntity.ok(Map.of(
                "nickname", nickname,
                "role", role,
                "message", "관리자 인증 성공"
        ));
    }
}

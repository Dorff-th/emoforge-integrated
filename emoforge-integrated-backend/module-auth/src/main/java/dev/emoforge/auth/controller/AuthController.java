package dev.emoforge.auth.controller;

import dev.emoforge.auth.dto.LoginRequest;
import dev.emoforge.auth.dto.LoginResponse;
import dev.emoforge.auth.dto.MemberDTO;
import dev.emoforge.auth.dto.SignUpRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.core.security.jwt.JwtTokenProvider;
import dev.emoforge.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * AuthController
 *
 * 인증/인가 관련 핵심 기능을 제공하는 컨트롤러.
 *
 * 제공 기능:
 * - 회원가입 (sign up)
 * - 로그인 (login)
 * - 현재 인증된 사용자 정보 조회 (me)
 * - 토큰 재발급 (refresh)
 * - 로그아웃 및 쿠키 삭제 (logout)
 *
 * JWT 기반 인증 시스템에서 AccessToken / RefreshToken을 발급 및 관리하며,
 * 도메인·SameSite·Secure 옵션이 적용된 HttpOnly 쿠키 형태로 토큰을 전달한다.
 * RefreshToken 유효성 검사 및 재발급, 세션 무효화 등 보안 처리를 모두 포함한다.
 */
@Tag(name = "Auth", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.cookie.access-domain}")
    private String accessDomain;

    @Value("${auth.cookie.refresh-domain}")
    private String refreshDomain;

    @Value("${auth.cookie.remove-domain}")
    private String removeDomain;

    @Value("${auth.cookie.secure}")
    private boolean secure;

    @Value("${auth.cookie.same-site}")
    private String sameSite;

    @Value("${auth.cookie.expiration.access-hours}")
    private long accessHours;

    @Value("${auth.cookie.expiration.refresh-days}")
    private long refreshDays;

    @Value("${auth.cookie.names.access}")
    private String accessCookieName;

    @Value("${auth.cookie.names.refresh}")
    private String refreshCookieName;

    // ---------------------------------------------------------
    // 🔹 회원가입 (not used)
    // ---------------------------------------------------------
    @Operation(
            summary = "회원가입 (not used)",
            description = "새로운 회원을 생성합니다. 유효성 검증 후 Member 객체를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터")
    })
    @PostMapping("/signup")
    public ResponseEntity<Member> signUp(@Valid @RequestBody SignUpRequest request) {
        Member member = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    // ---------------------------------------------------------
    // 🔹 로그인 (not used)
    // ---------------------------------------------------------
    @Operation(
            summary = "로그인  (not used)",
            description = """
                    사용자 로그인 처리 후 AccessToken / RefreshToken을 포함한 응답을 반환합니다.
                    토큰은 HttpOnly 쿠키에 저장되지 않으며 LoginResponse로 전달됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "잘못된 로그인 정보")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // 🔹 현재 사용자 정보 조회
    // ---------------------------------------------------------
    @Operation(
            summary = "현재 로그인한 사용자 정보 조회",
            description = """
                    인증된 사용자의 UUID를 기반으로 Member 정보를 조회합니다.
                    AccessToken이 유효해야 하며, 인증되지 않은 경우 예외가 발생합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/me")
    public MemberDTO getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal user) {
        if (user == null ) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        String uuid = user.getUuid();

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));;

        return new MemberDTO(member);
    }

    // ---------------------------------------------------------
    // 🔹 토큰 재발급 (Refresh)
    // ---------------------------------------------------------
    @Operation(
            summary = "토큰 재발급",
            description = """
                    RefreshToken 쿠키를 검증하여 새로운 AccessToken 및 RefreshToken을 발급합니다.
                    RefreshToken이 유효하지 않으면 401(UnAuthorized)을 반환합니다.
                    재발급된 토큰은 HttpOnly 쿠키로 클라이언트에 전달됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "RefreshToken 유효하지 않음"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {


        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // ✅ (변경) validateToken(refreshToken, false) → userSecret으로 검증
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken, false)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // 🛡 [2026-01-24 22:14 KST] access/admin 토큰으로 refresh 시도 방지
        if (!"refresh".equals(jwtTokenProvider.getTokenType(refreshToken))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token type");
        }


        //String memberUuid = jwtTokenProvider.getClaims(refreshToken).get("uuid", String.class);
        // 🔄 [2026-01-24 22:14 KST] refresh 토큰의 식별자는 JWT subject(uuid) 기준
        //    - claim("uuid") 의존 제거
        String memberUuid = jwtTokenProvider.getUuidFromToken(refreshToken);
        Member member = memberRepository.findByUuid(memberUuid)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 새 토큰 발급
        // 🔄 [2026-01-24 22:14 KST] generateAccessToken(subject=uuid) 기준으로 파라미터 정렬
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                member.getUuid(),
                member.getRole().name(),
                member.getUsername()
        );


        // 🔄 [2026-01-24 22:14 KST] refresh 토큰은 uuid만 필요
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(
                member.getUuid()
        );


        // ✅ (변경) domain을 auth 서브도메인으로 제한
        ResponseCookie accessCookie = ResponseCookie.from(accessCookieName, newAccessToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(accessDomain)
                .path("/")
                .maxAge(Duration.ofHours(accessHours))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(refreshCookieName, newRefreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(refreshDomain)
                .path("/")
                .maxAge(Duration.ofDays(refreshDays))
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok("Token refreshed");
    }

    // ---------------------------------------------------------
    // 🔹 로그아웃
    // ---------------------------------------------------------
    @Operation(
            summary = "로그아웃",
            description = """
                    AccessToken · RefreshToken 쿠키를 모두 삭제하여 로그아웃 처리합니다.
                    여러 도메인/환경에서 생성된 쿠키들을 제거하기 위해 다양한 Set-Cookie 변형을 함께 처리합니다.
                    OAuth2Login 잔여 세션도 함께 무효화합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        // (옵션) 세션 무효화 - OAuth2Login 사용 시 잔여 세션 끊기
        var session = request.getSession(false);
        if (session != null) session.invalidate();

        // 1) 과거에 '.127.0.0.1.nip.io' 로 발급된 토큰 쿠키 제거 (옵션 동일)
        response.addHeader("Set-Cookie",
                ResponseCookie.from(accessCookieName, "")
                        .domain(removeDomain)
                        .path("/")
                        .httpOnly(true)
                        .sameSite(sameSite)
                        .secure(secure)
                        .maxAge(0)
                        .build().toString()
        );
        response.addHeader("Set-Cookie",
                ResponseCookie.from(refreshCookieName, "")
                        .domain(removeDomain)
                        .path("/")
                        .httpOnly(true)
                        .sameSite(sameSite)
                        .secure(secure)
                        .maxAge(0)
                        .build().toString()
        );

        // 2) 혹시 모르는 변형들(호스트 전용/도메인 미지정)도 함께 정리
        response.addHeader("Set-Cookie",
                ResponseCookie.from(accessCookieName, "")
                        .path("/")
                        .httpOnly(true)
                        .sameSite(sameSite)
                        .secure(false)
                        .maxAge(0)
                        .build().toString()
        );
        response.addHeader("Set-Cookie",
                ResponseCookie.from(refreshCookieName, "")
                        .path("/")
                        .httpOnly(true)
                        .sameSite(sameSite)
                        .secure(secure)
                        .maxAge(0)
                        .build().toString()
        );

        // 3) JSESSIONID도 끊기 (OAuth2Login 세션 잔여 대비)
        response.addHeader("Set-Cookie",
                ResponseCookie.from("JSESSIONID", "")
                        .path("/")
                        .maxAge(0)
                        .build().toString()
        );

        return ResponseEntity.noContent().build();
    }
}

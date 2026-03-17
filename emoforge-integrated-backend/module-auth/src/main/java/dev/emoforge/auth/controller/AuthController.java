package dev.emoforge.auth.controller;

import dev.emoforge.auth.dto.LoginRequest;
import dev.emoforge.auth.dto.LoginResponse;
import dev.emoforge.auth.dto.MemberDTO;
import dev.emoforge.auth.dto.SignUpRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.service.LoginTokenService;
import dev.emoforge.auth.service.RefreshTokenService;
import dev.emoforge.core.properties.CookieProvider;
import dev.emoforge.core.security.jwt.JwtTokenVerifier;
import dev.emoforge.core.security.principal.CustomUserPrincipal;


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
    

    private final MemberRepository memberRepository;

    private final JwtTokenVerifier jwtTokenVerifier;

    private final LoginTokenService loginTokenService;

    //0317 추가
    private final CookieProvider cookieProvider;
    private final RefreshTokenService refreshTokenService;


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

        MemberDTO memberDTO = new MemberDTO(member);

        return memberDTO;
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

        if (refreshToken == null || !jwtTokenVerifier.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        //0317 추가
        refreshTokenService.validate(refreshToken);

        String memberUuid = jwtTokenVerifier.getUuidFromToken(refreshToken);
        Member member = memberRepository.findByUuid(memberUuid)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // ✅ 쿠키 + 토큰 재발급은 Service로 위임
        loginTokenService.handleTokenRefresh(response, member);

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

    //0317 수정 : refresh_token을 db에서 삭제하는 로직 추가
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // (옵션) OAuth2Login 세션 잔여 정리
        var session = request.getSession(false);
        if (session != null) session.invalidate();

        String refreshToken = cookieProvider.extractRefreshTokenFromCookie(request);

        //loginTokenService.handleLogout(response, LoginType.KAKAO);
        loginTokenService.handleLogout(refreshToken, response); // LoginType 무의미

        return ResponseEntity.noContent().build();
    }
}


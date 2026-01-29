package dev.emoforge.auth.controller;

import dev.emoforge.auth.dto.KakaoSignupRequest;
import dev.emoforge.auth.dto.KakaoSignupResponse;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.service.KakaoSignupService;
import dev.emoforge.auth.service.LoginTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import dev.emoforge.auth.enums.LoginType;

/**
 * KakaoSignupController
 *
 * 카카오 OAuth 신규 사용자에 대한 회원가입을 처리하는 컨트롤러.
 *
 * ✔ 실행 시점
 * 1) /api/auth/kakao (카카오 로그인 1단계) 호출
 * 2) 응답 status = NEED_AGREEMENT 인 경우
 * 3) 프론트엔드에서 이용약관(/auth/terms) 화면으로 이동
 * 4) 사용자가 약관 동의 → 이 엔드포인트(/signup) 호출
 *
 * ✔ 주요 기능
 * - 카카오 API에서 획득한 kakaoId, nickname 등을 기반으로 신규 회원(Member) 생성
 * - 회원 생성 후 JWT AccessToken / RefreshToken 쿠키 생성
 * - 회원가입이 완료되면 status=SINGED_UP 와 함께 uuid, nickname 반환
 *
 * ✔ 응답 헤더
 * - Set-Cookie : accessToken
 * - Set-Cookie : refreshToken
 *
 * 프론트엔드는 회원가입 성공 시 자동 로그인된 상태가 되며,
 * 적절한 라우터(/profile 등)로 이동할 수 있다.
 */
@Tag(
        name = "KakaoSignup",
        description = "카카오 기반 신규 회원가입(이용약관 동의 이후 단계)"
)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoSignupController {

    private final KakaoSignupService signupService;
    private final LoginTokenService loginTokenService;

    @Operation(
            summary = "카카오 신규 회원가입 처리",
            description = """
                    카카오 로그인 과정에서 기존 회원이 아닌 것으로 확인된 신규 사용자가
                    이용약관 동의 후 호출하는 회원가입 API입니다.
                                        
                    🔹 처리 내용
                    - kakaoId, nickname 등 정보를 통해 신규 Member 생성
                    - AccessToken / RefreshToken 쿠키 생성
                    - status = "SIGNED_UP" 반환
                    
                    프론트엔드는 회원가입 완료 후 자동 로그인 상태가 됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신규 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 필드 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 카카오 계정 (중복 가입)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody KakaoSignupRequest request,
            HttpServletResponse response
    ) {
        Member member = signupService.signupNewMember(request);

        loginTokenService.handleLoginSuccess(
                response,
                member,
                LoginType.KAKAO
        );

        return ResponseEntity.ok(
                Map.of(
                        "uuid", member.getUuid(),
                        "nickname", member.getNickname()
                )
        );
    }
}

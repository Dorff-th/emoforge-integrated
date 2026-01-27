package dev.emoforge.app.controller;

import dev.emoforge.auth.infra.kakao.KakaoCodeRequest;
import dev.emoforge.auth.infra.kakao.KakaoLoginResult;
import dev.emoforge.auth.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KakaoAuthController
 *
 * 카카오 OAuth2 로그인 요청을 처리하는 컨트롤러.
 *
 * ✔ 주요 동작
 * - 프론트엔드에서 전달한 "카카오 인가코드(code)"를 기반으로 카카오 API와 통신하여
 *   사용자 정보를 조회한 뒤, 기존 회원 여부를 판단한다.
 *
 * ✔ 처리 흐름
 * 1) 기존 회원(member)에 kakaoId가 존재하는 경우
 *      → 정상 로그인 처리
 *      → JWT AccessToken / RefreshToken 쿠키 생성
 *      → 응답 필드: status = "LOGIN_OK"
 *
 * 2) 기존 회원 정보가 없는 경우
 *      → 아직 가입되지 않은 사용자로 판단
 *      → JWT 쿠키 생성 ❌ (로그인 처리 없음)
 *      → 응답 필드: status = "NEED_AGREEMENT"
 *      → 프론트엔드에서 이용약관 화면으로 이동
 *
 * 이 API는 "로그인/회원가입 통합" 방식으로 동작하며,
 * 카카오 계정 기반 신규가입 플로우의 첫 단계이다.
 */
@Tag(
        name = "KakaoAuth",
        description = "카카오 OAuth2 기반 로그인 및 신규 가입 분기 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(
            summary = "카카오 로그인 처리",
            description = """
                    프론트엔드에서 전달한 카카오 인가코드(code)를 기반으로 로그인/회원가입 흐름을 처리합니다.
                    
                    🔹 기존 회원(Kakao ID가 등록된 경우)
                    - JWT AccessToken/RefreshToken 쿠키 생성
                    - status = "LOGIN_OK"
                    
                    🔹 신규 사용자(Kakao ID 미등록)
                    - 회원가입 전 단계로 판단
                    - JWT 쿠키 생성 없음
                    - status = "NEED_AGREEMENT"
                    
                    프론트엔드는 status 값을 기준으로
                    • LOGIN_OK → 자동 로그인 후 /profile 등으로 이동
                    • NEED_AGREEMENT → 이용약관 화면으로 이동
                    과 같이 라우팅하게 됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 또는 신규가입 분기 성공"),
            @ApiResponse(responseCode = "400", description = "카카오 API 호출 실패 또는 잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoCodeRequest request,
                                        HttpServletResponse response) {

        KakaoLoginResult result = kakaoAuthService.processKakaoLogin(request.code(), response);
        return ResponseEntity.ok(result);
    }
}

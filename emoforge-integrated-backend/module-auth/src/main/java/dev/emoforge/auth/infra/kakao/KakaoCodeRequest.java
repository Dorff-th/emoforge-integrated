package dev.emoforge.auth.infra.kakao;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * KakaoCodeRequest
 *
 * 카카오 OAuth 로그인 이후, 프론트엔드가 카카오 authorization 서버에서
 * 전달받은 "인가코드(code)"를 Auth-Service에 전달하기 위한 요청 DTO.
 *
 * ✔ 사용 맥락
 * - 사용자가 카카오 로그인 승인 → 카카오가 프론트엔드로 "code" 전달
 * - 프론트엔드는 이 code를 포함하여 Auth-Service `/api/auth/kakao`로 POST 요청
 *
 * ✔ code 예시
 *   code=IcAFj2k93kJdfk... (일회성 인가코드)
 *
 * 이 인가코드는 단 한 번만 사용할 수 있으며,
 * Auth-Service는 이 값을 기반으로 카카오 API를 호출하여
 * 사용자 정보를 조회한다.
 */
@Schema(description = "카카오 로그인 인가코드 요청 DTO")
public record KakaoCodeRequest(

        @Schema(
                description = "카카오 authorization 서버에서 전달받은 인가코드",
                example = "SplxlOBeZQQYbYS6WxSbIAo"
        )
        String code
) {}

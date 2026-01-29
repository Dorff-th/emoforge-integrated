package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.LoginType;
import dev.emoforge.auth.infra.kakao.KakaoClient;
import dev.emoforge.auth.infra.kakao.KakaoLoginResult;
import dev.emoforge.auth.infra.kakao.KakaoTokenResponse;
import dev.emoforge.auth.infra.kakao.KakaoUserResponse;
import dev.emoforge.auth.repository.MemberRepository;

import dev.emoforge.auth.token.JwtTokenIssuer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenIssuer jwtTokenIssuer;
    private final KakaoClient kakaoClient;  // 카카오 API 호출 담당 (아래 제공)

    private final LoginTokenService loginTokenService;

    public KakaoLoginResult processKakaoLogin(String code, HttpServletResponse response) {

        // 1) 인가 코드로 카카오 토큰 요청
        KakaoTokenResponse token = kakaoClient.getAccessToken(code);

        // 2) 카카오 프로필 조회
        KakaoUserResponse profile = kakaoClient.getUserInfo(token.accessToken());

        Long kakaoId = profile.id();
        String nickname = profile.properties().nickname();

        // 3) DB 조회
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoId);

        if (optionalMember.isEmpty()) {
            // 신규 회원 → 약관 동의 필요 → 회원 생성 X → JWT 발급 X
            return new KakaoLoginResult(
                    "NEED_AGREEMENT",
                    String.valueOf(kakaoId),
                    nickname
            );
        }

        // 4) 기존 회원 → 로그인 처리 → JWT 발급 + 쿠키 저장
        Member member = optionalMember.get();

        loginTokenService.handleLoginSuccess(
                response,
                member,
                LoginType.KAKAO
        );

        return new KakaoLoginResult(
                "LOGIN_OK",
                null,
                null
        );
    }
}

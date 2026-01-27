package dev.emoforge.auth.service;

import dev.emoforge.auth.dto.KakaoSignupRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.enums.Role;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.core.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSignupService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 쿠키 설정
    @Value("${auth.cookie.secure}")
    private boolean secure;

    @Value("${auth.cookie.access-domain}")
    private String accessDomain;

    @Value("${auth.cookie.same-site}")
    private String sameSite;

    public record SignupResult(
            String uuid,
            String nickname,
            ResponseCookie accessCookie,
            ResponseCookie refreshCookie
    ) {}

    @Transactional
    public SignupResult signupNewMember(KakaoSignupRequest req) {

        // 이미 가입된 kakaoId라면 예외
        memberRepository.findByKakaoId(req.getKakaoId())
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 가입된 카카오 계정입니다");
                });

        // 신규 UUID
        String uuid = UUID.randomUUID().toString();
        // 카카오로 부터 보안상 이메일 사용 불가 시 dummy 생성
        //String username = "kakao_" + UUID.randomUUID().toString().substring(0, 8) + "@dummy.local";
        String username = UUID.randomUUID().toString();
        String randomPassword = UUID.randomUUID().toString();
        String encoded = passwordEncoder.encode(randomPassword);

        // 신규 회원 생성
        Member member = Member.builder()
                .username(username)
                .email(username)
                .nickname(req.getNickname())
                .kakaoId(req.getKakaoId())
                .password(encoded)
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        memberRepository.save(member);

        // JWT 발급
        String accessToken = jwtTokenProvider.generateAccessToken(
                member.getUsername(),
                member.getRole().toString(),
                member.getUuid()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(
                member.getUuid()
        );

        // 쿠키 생성
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .domain(accessDomain)
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .domain(accessDomain)
                .maxAge(Duration.ofDays(7))
                .build();

        return new SignupResult(uuid, member.getNickname(), accessCookie, refreshCookie);
    }
}
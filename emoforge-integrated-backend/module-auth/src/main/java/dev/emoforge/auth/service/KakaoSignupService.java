package dev.emoforge.auth.service;

import dev.emoforge.auth.dto.KakaoSignupRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.enums.Role;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.token.JwtTokenIssuer;

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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signupNewMember(KakaoSignupRequest req) {

        memberRepository.findByKakaoId(req.getKakaoId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 카카오 계정입니다");
                });

        String uuid = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        String email = "kakao_" + UUID.randomUUID().toString().substring(0, 8) + "@dummy.local"; // 카카오 프로필로 직업 이메일 정보를 수집하지 않기에 랜덤 생성
        String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

        Member member = Member.builder()
                .uuid(uuid)
                .username(username)
                .email(email)
                .nickname(req.getNickname())
                .kakaoId(req.getKakaoId())
                .password(encodedPassword)
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(member);
    }
}


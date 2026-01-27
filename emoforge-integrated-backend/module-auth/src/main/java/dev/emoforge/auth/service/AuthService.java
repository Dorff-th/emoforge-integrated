package dev.emoforge.auth.service;

import dev.emoforge.auth.dto.LoginRequest;
import dev.emoforge.auth.dto.LoginResponse;
import dev.emoforge.auth.dto.SignUpRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.core.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public Member signUp(SignUpRequest request) {
        // 사용자명 중복 체크
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용 중인 사용자명입니다: " + request.getUsername());
        }
        
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        // Member 엔티티 생성 및 저장
        Member member = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .nickname(request.getNickname())
                .build();
        
        return memberRepository.save(member);
    }
    
    public LoginResponse login(LoginRequest request) {
        // 사용자명으로 회원 조회
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다"));
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다");
        }
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername(), member.getRole().name(), member.getUuid());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUuid());
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

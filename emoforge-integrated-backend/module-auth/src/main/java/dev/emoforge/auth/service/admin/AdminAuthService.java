package dev.emoforge.auth.service.admin;

import dev.emoforge.auth.dto.admin.AdminLoginRequest;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.Role;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.token.JwtTokenIssuer;
//import dev.emoforge.core.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 관리자 로그인 서비스
 * - Member 테이블 기반 인증 (role = ADMIN 필수)
 * - JWT 발급 후 Controller에서 쿠키로 내려줌
 */
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenIssuer jwtTokenIssuer;

    public Member authenticate(AdminLoginRequest request) {
        // 1. 사용자 조회
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 관리자 계정입니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 권한 확인
        if (member.getRole() != Role.ADMIN) {
            throw new RuntimeException("관리자 권한이 없습니다.");
        }

        // ✅ 여기까지가 AdminAuthService의 책임
        return member;
    }

}

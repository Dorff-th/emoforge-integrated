package dev.emoforge.auth.service;

import dev.emoforge.auth.dto.AvailabilityResponse;
import dev.emoforge.auth.dto.MemberProfileResponse;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

// service/MemberProfileService.java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberProfileService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AvailabilityResponse checkNickname(String nickname) {
        validateNicknameFormat(nickname);
        boolean exists = memberRepository.existsByNickname(nickname);
        return new AvailabilityResponse(!exists);
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse checkEmail(String email) {
        validateEmailFormat(email);
        boolean exists = memberRepository.existsByEmail(email);
        return new AvailabilityResponse(!exists);
    }

    public MemberProfileResponse updateNickname(String currentUserUuid, String nickname) {
        validateNicknameFormat(nickname);
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        Member member = memberRepository.findByUuid(currentUserUuid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        if (nickname.equals(member.getNickname())) {
            throw new IllegalArgumentException("현재 닉네임과 동일합니다.");
        }
        member.setNickname(nickname);
        return new MemberProfileResponse(member.getUuid(), member.getEmail(), member.getNickname());
    }

    public MemberProfileResponse updateEmail(String currentUserUuid, String email) {
        validateEmailFormat(email);
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        Member member = memberRepository.findByUuid(currentUserUuid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        if (email.equalsIgnoreCase(member.getEmail())) {
            throw new IllegalArgumentException("현재 이메일과 동일합니다.");
        }
        member.setEmail(email.toLowerCase(Locale.ROOT));
        //member.setUsername(email.toLowerCase(Locale.ROOT)); // username도 email과 동일하게 설정 ([2026-01-24 변경] email은 username에 저장하지 않기로 결정해서 해당코드 삭제해야함
        return new MemberProfileResponse(member.getUuid(), member.getEmail(), member.getNickname());
    }

    private void validateNicknameFormat(String nickname) {
        if (nickname == null || nickname.isBlank())
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        if (nickname.length() < 2 || nickname.length() > 20)
            throw new IllegalArgumentException("닉네임은 2~20자입니다.");
        // 필요 시 정규식: 영문/숫자/한글/특수 제한 등
        // if (!nickname.matches("^[a-zA-Z0-9가-힣_\\-]{2,20}$")) ...
    }

    private void validateEmailFormat(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        // 간단 검증
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다.");
    }

    public MemberProfileResponse getProfile(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + uuid));

        return new MemberProfileResponse(
                member.getUuid(),
                member.getEmail(),
                member.getNickname()
        );
    }

}


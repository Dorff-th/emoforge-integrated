package dev.emoforge.auth.service;

import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.core.security.service.MemberSecurityReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * MemberSecurityReader의 구현체.
 * Security 계층에서 요청한 회원 상태 조회를 실제 MemberRepository를 통해 처리하는 구현체.
 */
@Service
@RequiredArgsConstructor
public class MemberSecurityReaderImpl implements MemberSecurityReader {

    private final MemberRepository memberRepository;

    @Override
    public boolean isActive(String uuid) {
        return memberRepository.findStatusByUuid(uuid)
                .filter(status -> status == MemberStatus.ACTIVE)
                .isPresent();
    }
}

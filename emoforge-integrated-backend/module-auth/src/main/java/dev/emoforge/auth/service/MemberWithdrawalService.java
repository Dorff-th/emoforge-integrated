package dev.emoforge.auth.service;

import dev.emoforge.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 회원 탈퇴 신청 및 취소 로직 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberWithdrawalService {

    private final MemberRepository memberRepository;

    /**
     * 📌 회원 탈퇴 신청 처리
     * - deleted = true
     * - deleted_at = now()
     */
    @Transactional
    public void requestWithdrawal(String memberUuid) {
        log.info("🚪 회원 탈퇴 신청: uuid={}", memberUuid);

        int updated = memberRepository.markMemberAsDeleted(
                memberUuid,
                LocalDateTime.now()
        );

        if (updated == 0) {
            throw new IllegalStateException("탈퇴 처리 실패: 존재하지 않는 회원");
        }
    }

    /**
     * 📌 회원 탈퇴 취소 처리
     * - deleted = false
     * - deleted_at = null
     */
    @Transactional
    public void cancelWithdrawal(String memberUuid) {
        log.info("↩️ 회원 탈퇴 취소: uuid={}", memberUuid);

        int updated = memberRepository.cancelMemberDeletion(memberUuid);

        if (updated == 0) {
            throw new IllegalStateException("탈퇴 취소 실패: 존재하지 않는 회원");
        }
    }

}

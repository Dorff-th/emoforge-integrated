package dev.emoforge.auth.service.admin;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberAdminService {

    private final MemberRepository memberRepository;

    /**
     * 전체 회원 목록 조회
     */
    public List<Member> getAllMembers() {
        return memberRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 회원 상태 변경 (ACTIVE <-> INACTIVE)
     */
    public void updateStatus(String uuid, MemberStatus status) {
        int updated = memberRepository.updateStatusByUuid(uuid, status);
        if (updated == 0) {
            throw new IllegalArgumentException("해당 회원을 찾을 수 없습니다: " + uuid);
        }
    }

    /**
     * 탈퇴 여부 변경 (deleted = true/false)
     */
    @Transactional
    public Member updateDeleted(String uuid, boolean deleted) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다: " + uuid));

        if(deleted) {
            memberRepository.markMemberAsDeleted(
                    uuid,
                    LocalDateTime.now()
            );
            log.info("🚪 회원 탈퇴 신청으로 변경: uuid={}", uuid);
        } else {
            memberRepository.cancelMemberDeletion(uuid);
            log.info("🚪 회원 탈퇴 신청취소로 변경: uuid={}", uuid);
        }
        //deleted 값 갱신
        member.setDeleted(deleted);

        return member;
    }

}
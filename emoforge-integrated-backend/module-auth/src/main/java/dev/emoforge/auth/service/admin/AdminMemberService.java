package dev.emoforge.auth.service.admin;

import dev.emoforge.auth.dto.admin.AdminMemberListDTO;
import dev.emoforge.auth.dto.admin.MemberSearchCondition;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.MemberStatus;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.post.dto.internal.PageRequestDTO;
import dev.emoforge.post.dto.legacy.bff.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;

    /**
     * 전체 회원 목록 조회 - legacy
     */
    public Page<Member> getAllMembers(Pageable pageable) {
        return memberRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * 전체 회원 목록 조회 - Paging
     */
    public PageResponseDTO<AdminMemberListDTO> getMemberList(PageRequestDTO requestDTO, MemberSearchCondition searchCondition) {
        Page<Member> result = memberRepository.searchMembers(
                searchCondition.nickname(),
                searchCondition.deleted(),
                searchCondition.startDate(),
                searchCondition.endDate(),
                requestDTO.toPageable()
        );

        System.out.println("nickname = " + searchCondition.nickname());

        List<AdminMemberListDTO> dtoList =
                result.stream()
                        .map(AdminMemberListDTO::fromEntity)
                        .toList();

        System.out.println(result.getTotalElements());

        return new PageResponseDTO(requestDTO, result.getTotalElements(), result.stream().toList(), requestDTO.size());
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

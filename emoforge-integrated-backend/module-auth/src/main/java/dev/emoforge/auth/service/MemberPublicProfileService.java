// dev.emoforge.auth.service.MemberPublicProfileService.java
package dev.emoforge.auth.service;

import dev.emoforge.auth.dto.PublicProfileResponse;
import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.repository.MemberRepository;
import dev.emoforge.auth.service.external.AttachClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPublicProfileService {

    private final MemberRepository memberRepository;
    private final AttachClient attachClient;

    public PublicProfileResponse getPublicProfile(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String nickname = member.getNickname();

        String imageUrl = null;
        try {
            var imageResponse = attachClient.getProfileImage(uuid, "PROFILE_IMAGE");
            imageUrl = imageResponse.publicUrl();
        } catch (Exception e) {
            imageUrl = null; // 프로필 이미지가 없는 경우 예외 방지
        }

        return new PublicProfileResponse(nickname, imageUrl);
    }
}

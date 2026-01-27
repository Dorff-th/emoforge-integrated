package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.security.oauth.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오 응답 파싱
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");

        Map<String, Object> attributes = oAuth2User.getAttributes();

        Long kakaoId = ((Number) attributes.get("id")).longValue();
        //System.out.println("\n\n\n===kakaoId : " + kakaoId);

        // 신규/기존 회원 처리
        Member member = memberService.saveIfNotExist(kakaoId, nickname);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())),
                oAuth2User.getAttributes(),
                "id",                     // 카카오의 nameAttributeKey
                member.getUsername(),     // username
                member.getUuid(),         // String 타입 그대로
                member.getRole(),
                member
        );
    }
}

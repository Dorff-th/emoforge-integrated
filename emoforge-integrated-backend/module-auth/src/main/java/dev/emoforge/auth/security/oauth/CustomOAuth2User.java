package dev.emoforge.auth.security.oauth;

import dev.emoforge.auth.entity.Member;
import dev.emoforge.auth.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String username; // email (or dummy)
    private final String uuid;     // Member.uuid (String 타입)
    private final Role role;       // USER / ADMIN
    private final Member member;   // ✅ Member 엔티티 직접 포함

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String username,
                            String uuid,
                            Role role,
                            Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.username = username;
        this.uuid = uuid;
        this.role = role;
        this.member = member;
    }
}


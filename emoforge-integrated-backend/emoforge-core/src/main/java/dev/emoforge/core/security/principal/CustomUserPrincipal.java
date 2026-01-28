package dev.emoforge.core.security.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserPrincipal implements UserDetails {
    private final String username;
    private final String uuid;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(String username, String uuid, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.uuid = uuid;
        this.authorities = authorities;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return null; // JWT 기반이라 필요 없음
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

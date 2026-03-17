package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.RefreshToken;
import dev.emoforge.auth.repository.RefreshTokenRepository;
import dev.emoforge.auth.token.JwtTokenIssuer;
import dev.emoforge.core.properties.CookieProvider;
import dev.emoforge.core.security.jwt.JwtTokenParser;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 미사용 클래스
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenParser jwtTokenParser;

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }

    //로그인할때 refresh_token을 DB에 저장
    @Transactional
    public void save(String memberUuid, String refreshToken) {

        String tokenHash = hash(refreshToken);

        RefreshToken entity = new RefreshToken();
        entity.setMemberUuid(memberUuid);
        entity.setTokenHash(tokenHash);
        entity.setExpiredAt(jwtTokenParser.getRefreshTokenExpiry(refreshToken));

        refreshTokenRepository.save(entity);
    }

    //  refresh_token 검증
    @Transactional(readOnly = true)
    public RefreshToken validate(String refreshToken) {

        String tokenHash = hash(refreshToken);

        RefreshToken token = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    //0317 추가
    @Transactional
    public void rotateRefreshToken(String memberUuid, String newRefreshToken) {

        refreshTokenRepository.deleteByMemberUuid(memberUuid);
        save(memberUuid, newRefreshToken);
    }

}


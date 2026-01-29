package dev.emoforge.auth.service;

import dev.emoforge.auth.entity.RefreshToken;
import dev.emoforge.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 미사용 클래스
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(String memberUuid, String token) {
        refreshTokenRepository.deleteByMemberUuid(memberUuid);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberUuid(memberUuid)
                        .token(token)
                        .expired(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }
}


package dev.emoforge.auth.infra.kakao;

public record KakaoUserResponse(
        Long id,
        KakaoProperties properties
) {}

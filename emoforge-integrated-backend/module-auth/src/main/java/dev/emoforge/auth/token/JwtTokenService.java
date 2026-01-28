package dev.emoforge.auth.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenService {

    @Value("${jwt.secret.user}")    //폐기예정
    private String userSecret;

    @Value("${jwt.secret.admin}") // 폐기예정
    private String adminSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getUserKey() {
        return getSigningKey(userSecret);
    }

    //JwtTokenProvider.generateAccessToken 에서 가져옴
    public String createAccessToken(String username, String role, String uuid) {
        return Jwts.builder()
                .setSubject(uuid) // (변경) username -> uuid
                .claim("username", username) // (추가) 기존 subject였던 username은 claim으로 이동하여 필요시 사용
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                // ✅ (변경) userSecret으로 서명
                .signWith(getUserKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //JwtTokenProvider.generateRefreshToken 에서 가져옴
    public String createRefreshToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                //.claim("username", username)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                // ✅ (변경) userSecret으로 서명
                .signWith(getUserKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

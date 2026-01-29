package dev.emoforge.auth.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenIssuer {

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final SecretKey signingKey;

    public JwtTokenIssuer(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    //JwtTokenProvider.generateAccessToken 에서 가져옴
    public String createAccessToken(String role, String uuid) {
        return Jwts.builder()
                .setSubject(uuid) // (변경) username -> uuid
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                // ✅ (변경) userSecret으로 서명
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //JwtTokenProvider.generateRefreshToken 에서 가져옴
    public String createRefreshToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                // ✅ (변경) userSecret으로 서명
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


}

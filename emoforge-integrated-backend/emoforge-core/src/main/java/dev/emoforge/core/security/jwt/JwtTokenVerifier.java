package dev.emoforge.core.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class JwtTokenVerifier {

    @Value("${jwt.secret.user}")    //폐기예정
    private String userSecret;

    @Value("${jwt.secret.admin}") // 폐기예정
    private String adminSecret;

    private SecretKey getUserKey() {
        return getSigningKey(userSecret);
    }

    private SecretKey getAdminKey() {
        return getSigningKey(adminSecret);
    }

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //JwtTokenProvider.validateToken() 에서 가져옴 : setSigningKey에서 adminKey,UserKey 폐기예정
    public boolean validateToken(String token, boolean isAdmin) {

        try {
            String which = isAdmin ? "ADMIN" : "USER";
            log.info("🔑 validateToken(): using {} secret", which);
            // ✅ (변경) isAdmin 여부에 따라 다른 secret으로 검증
            Jwts.parserBuilder()
                    .setSigningKey(isAdmin ? getAdminKey() : getUserKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token");
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token", ex);
        }
        return false;
    }

    public Claims getClaims(String token) {
        // 우선 Base64로 payload만 잠깐 파싱 (검증은 하지 않음)
        String[] parts = token.split("\\.");
        String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        boolean isAdmin = payloadJson.contains("\"role\":\"ADMIN\"");

        return Jwts.parserBuilder()
                .setSigningKey(isAdmin ? getAdminKey() : getUserKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public String getUuidFromToken(String token) {
        // 🔄 [2026-01-24] uuid는 JWT subject에서 직접 추출
        return getClaims(token).getSubject();
    }

    /**
     * role 추출
     */
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    //getUsernameFromToken(String) <- JwtTokenProvider에 썼던것으로 제거 예정
    //getTokenType(String) 제거예정


}

package dev.emoforge.core.security.jwt;

import dev.emoforge.core.security.principal.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenVerifier jwtTokenVerifier;     // 🔁 변경: 토큰 검증 전담
    private final JwtTokenParser  jwtTokenParser ;       // 🔁 변경: Claims 파싱 전담


    public JwtAuthenticationFilter(JwtTokenVerifier jwtTokenVerifier,
                                   JwtTokenParser jwtTokenParser) {
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.jwtTokenParser = jwtTokenParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ (유지) 로그인/리프레시 경로는 필터 제외
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/admin/login") ||
                path.startsWith("/api/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        // 🔁 변경: validateFunction → JwtTokenVerifier 사용
        if (token != null && jwtTokenVerifier.validateToken(token)) {
            try {

                Claims claims = jwtTokenParser.parseClaims(token);

                String uuid = claims.getSubject(); // sub = member_uuid
                String role = claims.get("role", String.class);

                CustomUserPrincipal principal =
                        new CustomUserPrincipal(
                                uuid,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                Authentication auth =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                principal.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.error("JWT 인증 중 오류 발생", e);
                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // ✅ (유지) Authorization 헤더 우선
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // ✅ (유지) 쿠키에서 access_token
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}



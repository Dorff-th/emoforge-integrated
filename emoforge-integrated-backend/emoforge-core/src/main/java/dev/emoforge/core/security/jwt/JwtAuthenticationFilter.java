package dev.emoforge.core.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Function;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ✅ (변경) 기존: JwtTokenProvider 직접 의존 → 함수형으로 주입받도록 수정
    //    SecurityConfig에서 user/admin 여부를 람다로 넘길 수 있게 함.
    private final Function<String, Boolean> validateFunction;           // 토큰 검증 함수
    private final Function<String, Authentication> authenticationFunction; // 인증 생성 함수

    // ✅ (추가) 생성자 직접 정의 (제네릭 타입 명시)
    public JwtAuthenticationFilter(Function<String, Boolean> validateFunction,
                                   Function<String, Authentication> authenticationFunction) {
        this.validateFunction = validateFunction;
        this.authenticationFunction = authenticationFunction;
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

        // ✅ (변경) JwtTokenProvider 직접 호출 → 주입받은 함수 사용
        if (token != null && validateFunction.apply(token)) {
            try {
                Authentication auth = authenticationFunction.apply(token);
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

        // ✅ (유지) 쿠키에서 access_token / admin_token 둘 다 허용
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
                if ("admin_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}



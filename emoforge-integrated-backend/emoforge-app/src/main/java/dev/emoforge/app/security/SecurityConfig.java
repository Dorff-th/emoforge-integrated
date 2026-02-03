package dev.emoforge.app.security;

import dev.emoforge.auth.token.JwtTokenIssuer;
import dev.emoforge.core.security.config.SecurityProperties;
import dev.emoforge.core.security.jwt.JwtAuthenticationFilter;
import dev.emoforge.auth.service.CustomOAuth2UserService;
import dev.emoforge.core.security.jwt.JwtTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    private final Environment env;

    private final SecurityProperties corsProps;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // 🔓 Auth 공개 엔드포인트
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/refresh",
                                "/api/auth/health",
                                "/api/auth/public/**",
                                "/api/auth/kakao",
                                "/api/auth/kakao/signup"
                        ).permitAll()

                        // 🔐 관리자 API
                        .requestMatchers("/api/auth/admin/login").permitAll()
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")

                        // 🔐 나머지 Auth API
                        .requestMatchers("/api/auth/**").authenticated()

                        // (임시) 다른 서비스는 나중에
                        .anyRequest().permitAll()
                )

                // ✅ 단일 JWT 필터
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            config.setAllowedOriginPatterns(corsProps.allowedOriginPatterns());
        } else {
            config.setAllowedOrigins(corsProps.allowedOrigins());
        }
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}


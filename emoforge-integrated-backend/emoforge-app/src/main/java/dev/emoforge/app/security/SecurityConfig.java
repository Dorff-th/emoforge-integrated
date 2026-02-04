package dev.emoforge.app.security;

import dev.emoforge.auth.token.JwtTokenIssuer;
import dev.emoforge.core.security.config.SecurityProperties;
import dev.emoforge.core.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static dev.emoforge.app.security.endpoint.AuthEndpoints.*;
import static dev.emoforge.app.security.endpoint.AttachEndpoints.*;
import static dev.emoforge.app.security.endpoint.PostEndpoints.*;
import static dev.emoforge.app.security.endpoint.DiaryEndpoints.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    private final Environment env;

    private final SecurityProperties corsProps;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // ==== Auth ====
                        .requestMatchers(AUTH_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(AUTH_AUTHENTICATED_ENDPOINTS).authenticated()
                        .requestMatchers(AUTH_ADMIN_ENDPOINTS).hasRole("ADMIN")
                        // ==== Attach ====
                        .requestMatchers(ATTACH_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, ATTACH_AUTHENTICATED_GET_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.POST, ATTACH_AUTHENTICATED_POST_ENDPOINTS).authenticated()
                        .requestMatchers(ATTACH_AUTHENTICATED_ENDPOINTS).authenticated()
                        // ==== Post ====
                        .requestMatchers(HttpMethod.GET, POST_PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, POST_AUTHENTICATED_GET_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.POST, POST_AUTHENTICATED_POST_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.PUT, POST_AUTHENTICATED_PUT_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.DELETE, POST_AUTHENTICATED_DELETE_ENDPOINTS).authenticated()
                        .requestMatchers(POST_ADMIN_ENDPOINTS).hasRole("ADMIN")
                        // ==== Diary ====
                        .requestMatchers(DIARY_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(DIARY_AUTHENTICATED_ENDPOINTS).authenticated()
                        .anyRequest().authenticated()
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


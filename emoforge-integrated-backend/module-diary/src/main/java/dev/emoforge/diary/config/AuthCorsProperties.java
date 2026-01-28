package dev.emoforge.diary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "diary.cors")
public record AuthCorsProperties(
        List<String> allowedOrigins
) {}

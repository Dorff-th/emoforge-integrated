package dev.emoforge.post.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "post.cors")
public record AuthCorsProperties(
        List<String> allowedOrigins
) {}

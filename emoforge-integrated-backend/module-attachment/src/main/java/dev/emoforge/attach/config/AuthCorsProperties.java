package dev.emoforge.attach.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "attach.cors")
public record AuthCorsProperties(
        List<String> allowedOrigins
) {}

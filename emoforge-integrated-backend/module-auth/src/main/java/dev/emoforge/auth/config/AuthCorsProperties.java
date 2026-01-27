
package dev.emoforge.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "auth.cors")
public record AuthCorsProperties(
        List<String> allowedOrigins,
        List<String> allowedOriginPatterns
) {}

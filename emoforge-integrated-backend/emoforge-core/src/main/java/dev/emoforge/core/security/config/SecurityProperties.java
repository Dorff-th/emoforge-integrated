
package dev.emoforge.core.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security.cors")
public record SecurityProperties(
        List<String> allowedOrigins,
        List<String> allowedOriginPatterns
) {}


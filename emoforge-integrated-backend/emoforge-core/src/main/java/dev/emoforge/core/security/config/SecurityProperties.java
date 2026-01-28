
package dev.emoforge.core.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "auth.cors")
public record SecurityProperties(
        List<String> allowedOrigins,
        List<String> allowedOriginPatterns
) {}


/**
 * AuthCorsProperties
 * @ConfigurationProperties(prefix = "auth.cors")
 * @ConfigurationProperties(prefix = "attach.cors")
 * @ConfigurationProperties(prefix = "post.cors")
 * @ConfigurationProperties(prefix = "diary.cors")
 */
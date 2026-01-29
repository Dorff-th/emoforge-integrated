package dev.emoforge.app.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperties {

    private Common common;
    private Token access;
    private Token refresh;

    @Deprecated
    private Admin admin;

    @Getter @Setter
    public static class Common {
        private String domain;
        private boolean secure;
        private String sameSite;
    }

    @Getter @Setter
    public static class Token {
        private String name;
        private boolean httpOnly;
        private Integer expirationHours;   // access
        private Integer expirationDays;    // refresh
    }

    @Getter
    @Setter
    @Deprecated
    public static class Admin {
        private String name;
        private boolean httpOnly;
        private long maxAgeSeconds;
    }
}

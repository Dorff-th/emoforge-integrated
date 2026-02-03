package dev.emoforge.app.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "security.cookie")
@Getter
@Setter
public class CookieProperties {

    private Common common;
    private Token access;
    private Token refresh;

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
        private Integer expirationHours; // access
        private Integer expirationDays;  // refresh
    }
}


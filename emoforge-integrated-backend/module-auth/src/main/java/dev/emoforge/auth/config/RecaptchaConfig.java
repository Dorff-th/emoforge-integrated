package dev.emoforge.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecaptchaConfig {

    @Value("${recaptcha.enabled:true}")
    private boolean recaptchaEnabled;

    public boolean isRecaptchaEnabled() {
        return recaptchaEnabled;
    }
}

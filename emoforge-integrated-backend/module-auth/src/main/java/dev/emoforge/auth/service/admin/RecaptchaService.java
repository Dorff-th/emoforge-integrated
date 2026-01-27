package dev.emoforge.auth.service.admin;

import dev.emoforge.auth.config.RecaptchaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecaptchaService {

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    private final RecaptchaConfig recaptchaConfig;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String token) {

        log.debug("\n\n\n===== recaptchaConfig.isRecaptchaEnabled() " + recaptchaConfig.isRecaptchaEnabled());
        // ✅ 개발환경에서는 바로 통과
        if (!recaptchaConfig.isRecaptchaEnabled()) {
            //log.debug("\n\n\n\n=======reCAPCHA 개발중 일때는 그냥 통과!  ");
            return true;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", secretKey);
            params.add("response", token);

            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, params, Map.class);
            Map<String, Object> body = response.getBody();

            return body != null && Boolean.TRUE.equals(body.get("success"));
        } catch (Exception e) {
            return false;
        }
    }
}


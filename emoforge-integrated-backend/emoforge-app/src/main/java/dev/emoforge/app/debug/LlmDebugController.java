package dev.emoforge.app.debug;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping("/api/debug/llm")
@ConditionalOnProperty(name = "debug.endpoints.enabled", havingValue = "true")
public class LlmDebugController {

    private final RestClient restClient;

    @Value("${llm.base-url}")
    private String llmBaseUrl;

    public LlmDebugController(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        // LLM 서비스에 "가벼운" GET 엔드포인트가 있으면 그걸 호출
        // 없다면 LLM 쪽에 /health 같은 거 하나 만드는 걸 추천
        String url = llmBaseUrl.replaceAll("/+$", "") + "/health";
        Map<?, ?> res = restClient.get()
                .uri(url)
                .retrieve()
                .body(Map.class);

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "called", url,
                "llmResponse", res
        ));
    }
}

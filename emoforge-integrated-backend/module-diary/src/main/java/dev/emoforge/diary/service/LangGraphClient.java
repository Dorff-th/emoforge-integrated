package dev.emoforge.diary.service;

import dev.emoforge.diary.dto.music.LangGraphRequest;
import dev.emoforge.diary.dto.music.LangGraphResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LangGraphClient {

    private final RestTemplate restTemplate;

    @Value("${langgraph.base-url}")
    private String baseUrl;

    @Value("${langgraph.api-key:local-dev-key}")
    private String apiKey;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }

    /**
     * LangGraph-Service에 회고 요약 요청
     */
    public String requestSummary(LocalDate date, String fullContent) {
        String url = baseUrl + "/diary/gpt/summary";

        Map<String, Object> body = Map.of(
                "date", date.toString(),
                "content", fullContent
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object summary = response.getBody().get("summary");
            return summary != null ? summary.toString() : "";
        }

        throw new RuntimeException("LangGraph 응답이 비정상입니다: " + response.getStatusCode());
    }

    /**
     * 감정 기반 음악 추천 요청 (신규)
     */
    public LangGraphResponse requestMusicRecommendations(LangGraphRequest request) {
        String url = baseUrl + "/diary/gpt/music/recommendations/simple";

        System.out.println("\n\n\n====url : " + url);

        Map<String, Object> body = Map.of(
                "emotionScore", request.getEmotionScore(),
                "feelingKo", request.getFeelingKo(),
                "diaryContent", request.getContent(),
                "artistPreferences", request.getArtistPreferences()
        );

        //ResponseEntity<LangGraphResponse> response = restTemplate.postForEntity(url, new HttpEntity<>(body, createHeaders()), LangGraphResponse.class);
        ResponseEntity<LangGraphResponse> response = restTemplate.postForEntity(url, body, LangGraphResponse.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("LangGraph 음악 추천 응답이 비정상입니다: " + response.getStatusCode());
    }
}


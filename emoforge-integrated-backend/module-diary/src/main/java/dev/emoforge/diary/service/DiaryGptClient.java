package dev.emoforge.diary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component("diaryGptClient")
@RequiredArgsConstructor
public class DiaryGptClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String userPrompt, GptRole role) {

        String systemMessage = getSystemMessage(role);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "gpt-5-mini", // 또는 gpt-3.5-turbo
                "messages", List.of(
                        Map.of("role", "system", "content", systemMessage),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", request, Map.class);

        // 응답에서 content 파싱
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }

    public String getSummaryFromGpt(String prompt) {
        // 실제 OpenAI 연동 또는 테스트용 목 데이터
        return "오늘은 평소보다 일찍 일어나며 하루를 시작했고, 집중력 있게 업무를 마무리했다.";
    }


    public String getFeedbackFromGpt(String prompt) {
        // TODO: 실제 GPT 연동 or mock
        return "오늘도 실천을 멈추지 않은 당신, 정말 대단해요! 계속해서 루틴을 이어가 봐요 😊";
    }


    private String getSystemMessage(GptRole role) {
        return switch (role) {
            case FEELING_TRANSLATOR -> "You are an expert in expressing Korean emotions in natural English sentences.";
            case DIARY_SUMMARIZER -> "당신은 사용자의 회고 내용을 간결하고 따뜻하게 요약하는 한국어 요약 전문가입니다.";
            case FEEDBACK_COACH -> "당신은 사용자의 감정에 공감하고 조언을 주는 따뜻한 피드백 전문가입니다.";
        };
    }


}

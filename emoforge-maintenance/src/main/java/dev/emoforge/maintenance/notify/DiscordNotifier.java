package dev.emoforge.maintenance.notify;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DiscordNotifier {

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void send(String message) {
        Map<String, String> payload = Map.of("content", message);
        restTemplate.postForObject(webhookUrl, payload, String.class);
    }
}
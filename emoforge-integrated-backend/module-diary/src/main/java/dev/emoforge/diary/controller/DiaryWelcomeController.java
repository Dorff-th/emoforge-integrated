package dev.emoforge.diary.controller;

import dev.emoforge.core.security.principal.CustomUserPrincipal;
import dev.emoforge.diary.service.GptService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Hidden
@RestController
@RequestMapping("/api/diary/welcome")
@RequiredArgsConstructor
public class DiaryWelcomeController {

    final private GptService gptService;

    @GetMapping
    public String welcome() {
        return "Diary-Service is running!";
    }

    @GetMapping("/jwt")
    public String testJwt(Authentication authentication) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();

        return "Diary-Service : JWT member_uuid = " + memberUuid;
    }


}

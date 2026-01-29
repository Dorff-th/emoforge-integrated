package dev.emoforge.post.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class PostWelcomeController {

    @GetMapping("/api/posts/welcome")
    public String welcome() {
        return "Post-Service is running!";
    }
}

package dev.emoforge.post.admin.controller;

import dev.emoforge.core.security.principal.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class AdminTestController {

    @GetMapping("/api/posts/admin/test/jwt")
    public String testJwt(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String memberUuid = principal.getUuid();
        return "Post-Service : JWT member_uuid (ADMIN ROLE) = " + memberUuid;
    }
}

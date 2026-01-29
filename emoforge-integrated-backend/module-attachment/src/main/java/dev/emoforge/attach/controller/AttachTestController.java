package dev.emoforge.attach.controller;

import dev.emoforge.core.security.principal.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT 연동을 검증하기 위해 사용했던 테스트용 Controller.
 * 현재 운영 기능에서는 사용되지 않으며, 내부 개발 확인용 엔드포인트이다.
 */
@Tag(
        name = "Attachment-Service Internal/Test API",
        description = "개발 단계에서 JWT 연동 검증을 위해 사용된 테스트 엔드포인트 (현재는 운영 기능 아님)"
)
@RestController
public class AttachTestController {

    @Operation(
            summary = "JWT 인증 연동 테스트 (현재 사용되지 않음)",
            description = """
                    Auth-Service에서 발급된 JWT가 attachment-service에서 정상적으로 검증되는지 확인하기 위해 
                    개발 과정 초기에 사용되었던 테스트 API입니다.
                    
                    ✔ 현재는 실제 서비스 기능에 사용되지 않으며,  
                    ✔ 내부 개발 과정에서만 사용되었습니다.
                    
                    반환값: Authentication principal 값 (member_uuid)
                    """
    )
    @GetMapping("/api/attach/test/jwt")
    public String testJwt(Authentication authentication) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        String memberUuid = principal.getUuid();

        return "JWT member_uuid = " + memberUuid;
    }
}

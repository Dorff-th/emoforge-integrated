package dev.emoforge.attach.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Attachment-Service 헬스 체크용 Controller.
 * 서비스 동작 여부 및 기본 정보를 반환한다.
 */
@Tag(
        name = "Attachment Service Health API",
        description = "attachment-service의 상태를 확인하기 위한 헬스 체크 엔드포인트"
)
@RestController
@RequestMapping("/api/attach/health")
public class HealthController {

    @Operation(
            summary = "Attachment-Service 헬스 체크",
            description = """
                    attachment-service의 현재 상태를 간단한 JSON 형태로 반환합니다.
                    
                    ✔ 반환 정보
                    - status: 서비스 상태 (OK)
                    - service: attachment-service
                    - timestamp: 현재 서버 시간
                    """
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OK");
        status.put("service", "attachment-service");
        status.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }
}

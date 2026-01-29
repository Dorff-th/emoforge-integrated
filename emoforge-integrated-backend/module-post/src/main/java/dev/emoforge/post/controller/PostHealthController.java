package dev.emoforge.post.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Post-Service의 상태 체크용 엔드포인트.
 *
 * 이 API는 서비스가 정상적으로 동작 중인지 확인하기 위해
 * 모니터링 시스템 또는 외부 헬스 체크(예: AWS Load Balancer)가 호출한다.
 *
 * ✔ 인증 불필요
 * ✔ 단순한 상태 확인용
 * ✔ 모든 MSA 서비스에서 공통으로 제공되는 Health API
 */
@Hidden
@RestController
@RequestMapping("/api/posts/health")
public class PostHealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OK");
        status.put("service", "post-service");
        status.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }
}

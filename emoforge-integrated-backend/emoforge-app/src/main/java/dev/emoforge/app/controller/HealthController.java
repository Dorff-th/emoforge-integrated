package dev.emoforge.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HealthController
 *
 * Auth-Service의 헬스 체크 엔드포인트를 제공하는 컨트롤러.
 * - 서비스 상태(status) 확인
 * - 서비스 이름(service)
 * - 현재 서버 시간(timestamp)
 *
 * 주로 로드밸런서, 컨테이너 오케스트레이션(Docker, Kubernetes),
 * 모니터링 시스템 등에서 Auth-Service의 동작 여부를 확인하기 위해 사용된다.
 */
@Tag(name = "Health", description = "서비스 헬스 체크 API")
@RestController
@RequestMapping("/api/auth/health")
public class HealthController {

    @Operation(
            summary = "Auth-Service 헬스 체크",
            description = "Auth-Service가 정상적으로 동작 중인지 반환합니다. "
                    + "상태(status), 서비스명(service), 현재 서버 시각(timestamp)을 포함한 JSON 응답을 제공합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서비스 동작 중 (OK)")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OK");
        status.put("service", "auth-service");
        status.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }
}

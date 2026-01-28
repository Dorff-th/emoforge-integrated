package dev.emoforge.attach.controller;

import dev.emoforge.attach.domain.Attachment;
import dev.emoforge.attach.domain.UploadType;
import dev.emoforge.attach.repository.AttachmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 사용자 프로필 이미지 조회 전용 Controller.
 *
 * 이 엔드포인트는 프론트엔드에서 직접 호출되는 것이 아니라,
 * Auth-Service의 Public Profile API
 *   → GET /api/auth/public/members/{uuid}/profile
 * 에서 FeignClient를 통해 내부적으로 호출된다.
 *
 * 즉, auth-frontend는 auth-service만 호출하고,
 * 실제 프로필 이미지 URL 조회는 attach-service가 맡는 구조(BFF 스타일).
 *
 * 이 API는 "특정 사용자(memberUuid)가 업로드한 최신 프로필 이미지"를 조회하여
 * public URL 형태로 반환한다.
 */
@Tag(
        name = "Public Profile Image API",
        description = "Auth-Service에서 Feign으로 호출하는 프로필 이미지 조회 API (BFF 구조)"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attach")
@Slf4j
public class PublicAttachController {
    private  final AttachmentRepository attachmentRepository;

    @Operation(
            summary = "사용자 프로필 이미지 조회",
            description = """
                    특정 사용자의 최신 프로필 이미지 URL을 반환합니다.
                    
                    ✔ 이 API는 프론트엔드에서 직접 호출되지 않습니다.
                    ✔ Auth-Service의 Public Profile API가 FeignClient로 내부 호출합니다.
                    
                    호출 흐름:
                    auth-frontend
                      → /api/auth/public/members/{uuid}/profile
                      → Auth-Service 내부에서 Feign 호출
                      → Attach-Service (본 API)
                    
                    반환값:
                    - publicUrl: 최신 프로필 이미지의 공개 URL (없으면 null)
                    """
    )
    @GetMapping("/public/profile")
    public ResponseEntity<Map<String, String>> getProfileImage(
            @RequestParam("memberUuid") String memberUuid,
            @RequestParam("uploadType") UploadType uploadType
    ) {

        Attachment attach = attachmentRepository
                .findTopByMemberUuidAndUploadTypeOrderByUploadedAtDesc(memberUuid, uploadType)
                .orElse(null);

        String url = attach != null ? attach.getPublicUrl() : null;

        //log.debug("url : " + url);

        return ResponseEntity.ok(Map.of("publicUrl", url));
    }
}

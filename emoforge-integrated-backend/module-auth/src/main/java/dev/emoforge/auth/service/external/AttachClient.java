package dev.emoforge.auth.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "attach-service", url = "${service.attach.url}")
public interface AttachClient {

    // ✅ memberUuid와 uploadType으로 프로필 이미지 조회
    @GetMapping("/api/attach/public/profile")
    PublicImageResponse getProfileImage(
            @RequestParam("memberUuid") String memberUuid,
            @RequestParam("uploadType") String uploadType
    );

    record PublicImageResponse(String publicUrl) {}
}

package dev.emoforge.auth.service.external;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * FeignClient를 걷어내는 중
 * 삭제 예정
 */
@Component
@Primary
public class DummyAttachClient implements AttachClient {

    @Override
    public PublicImageResponse getProfileImage(String memberUuid, String uploadType) {
        return null;
    }
}
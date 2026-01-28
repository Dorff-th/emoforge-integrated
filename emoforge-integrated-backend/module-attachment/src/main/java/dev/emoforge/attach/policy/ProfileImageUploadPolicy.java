package dev.emoforge.attach.policy;

import dev.emoforge.attach.domain.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  사용자 프로필 이미지 첨부파일 정책 결정 : 업로드 디렉토리, 사이제 제한, 중복정책
 */
@Component
public class ProfileImageUploadPolicy implements UploadPolicy {

    @Value("${file.upload.path.profile-image.base-dir}")
    private String baseDir;

    @Value("${file.upload.path.profile-image.public-url}")
    private String publicUrl;

    @Override
    public UploadType getType() {
        return UploadType.PROFILE_IMAGE;
    }

    @Override
    public String getBaseDir() { return baseDir; }

    @Override
    public String getPublicUrl(String fileName) { return publicUrl + fileName; }

    @Override
    public long getMaxSize() { return 2 * 1024 * 1024; } // 2MB

    @Override
    public List<String> allowedExtensions() {
        return List.of("jpg", "jpeg", "png");
    }
}

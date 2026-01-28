package dev.emoforge.attach.policy;

import dev.emoforge.attach.domain.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  에디터 이미지 첨부파일 정책 결정 : 업로드 디렉토리, 사이제 제한, 중복정책
 */
@Component
public class EditorImageUploadPolicy implements UploadPolicy {

    @Value("${file.upload.path.editor-images.base-dir}")
    private String baseDir;

    @Value("${file.upload.path.editor-images.public-url}")
    private String publicUrl;

    @Override
    public UploadType getType() {
        return UploadType.EDITOR_IMAGE;
    }

    @Override
    public String getBaseDir() { return baseDir; }

    @Override
    public String getPublicUrl(String fileName) { return publicUrl + fileName; }

    @Override
    public long getMaxSize() { return 5 * 1024 * 1024; } // 5MB

    @Override
    public List<String> allowedExtensions() {
        return List.of("jpg", "jpeg", "png", "gif", "webp");
    }
}

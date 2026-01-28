package dev.emoforge.attach.policy;

import dev.emoforge.attach.domain.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 일반 첨부파일 정책 결정 : 업로드 디렉토리, 사이제 제한, 중복정책
 */

@Component
public class AttachmentUploadPolicy implements UploadPolicy {

    @Value("${file.upload.path.attachments}")
    private String baseDir;

    @Override
    public UploadType getType() {
        return UploadType.ATTACHMENT;
    }

    @Override
    public String getBaseDir() { return baseDir; }

    @Override
    public String getPublicUrl(String fileName) {
        // 다운로드 엔드포인트를 통해 접근
        return "/api/attachments/download/" + fileName;
    }

    @Override
    public long getMaxSize() { return 10 * 1024 * 1024; } // 10MB

    @Override
    public List<String> allowedExtensions() {
        return List.of("pdf", "docx", "xlsx", "zip", "md", "hwp", "hwpx");
    }
}

package dev.emoforge.attach.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileDownloadUtil {

    @Value("${file.upload.path.attachments}")
    private String attachmentsBaseDir;

    /**
     * 지정된 경로의 파일을 다운로드 가능한 형태로 반환
     *
     * @param relativePath   DB 등에 저장된 상대경로 (ex: "d054aa8a696746d9afa74011b9739123.md")
     * @param originalName   사용자가 업로드한 원본 파일명
     */
    public ResponseEntity<Resource> getDownloadResponse(String relativePath, String originalName) {
        try {
            // ✅ 컨테이너 내부 기준 경로로 합침
            String fullPath = Paths.get(attachmentsBaseDir, relativePath).toString();
            File file = new File(fullPath);

            if (!file.exists()) {
                throw new RuntimeException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
            }

            FileSystemResource resource = new FileSystemResource(file);
            String encodedFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                    .body(resource);

        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
        }
    }
}


package dev.emoforge.attach.util;

import dev.emoforge.attach.dto.UploadedFileResult;
import dev.emoforge.attach.policy.UploadPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUploadUtil {

    public UploadedFileResult saveFile(MultipartFile file, UploadPolicy policy) throws IOException {
        // 1. 디렉토리 보장
        Path baseDir = Paths.get(policy.getBaseDir());
        Files.createDirectories(baseDir);

        // 2. 파일명 유니크 처리
        //String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf(".") + 1); // "png"
        }

        String uuid = UUID.randomUUID().toString().replace("-", ""); // "a12f3c45..."
        String uniqueName = ext.isEmpty() ? uuid : uuid + "." + ext;
        // 3. 실제 저장
        Path savePath = baseDir.resolve(uniqueName);
        file.transferTo(savePath.toFile());

        // 4. public URL
        String publicUrl = policy.getPublicUrl(uniqueName);

        return UploadedFileResult.builder()
                .fileName(uniqueName)
                .originFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .publicUrl(publicUrl)
                .build();
    }

    public boolean deleteFile(String filePath) {
        return new File(filePath).delete();
    }
}

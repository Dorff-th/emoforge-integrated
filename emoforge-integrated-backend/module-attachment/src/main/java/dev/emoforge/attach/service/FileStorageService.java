package dev.emoforge.attach.service;

import dev.emoforge.attach.config.FileUploadProperties;
import dev.emoforge.attach.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileUploadProperties fileUploadProperties;

    public void deleteFile(Attachment attachment) {
        Path filePath = resolveFilePath(attachment);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete file: " + filePath, e);
        }
    }

    private Path resolveFilePath(Attachment attachment) {
        String baseDir = switch (attachment.getUploadType()) {
            case EDITOR_IMAGE -> fileUploadProperties.getEditorImages().getBaseDir();
            case ATTACHMENT -> fileUploadProperties.getAttachments();
            case PROFILE_IMAGE -> fileUploadProperties.getProfileImage().getBaseDir();
        };

        return Paths.get(baseDir).resolve(attachment.getFileName());
    }
}

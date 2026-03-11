package dev.emoforge.attach.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.upload.path")
@Getter
@Setter
public class FileUploadProperties {

    private UploadPath editorImages;
    private String attachments;
    private UploadPath profileImage;

    @Getter
    @Setter
    public static class UploadPath {
        private String baseDir;
        private String publicUrl;
    }
}

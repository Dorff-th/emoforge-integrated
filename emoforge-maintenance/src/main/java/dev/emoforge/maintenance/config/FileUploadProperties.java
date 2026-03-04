package dev.emoforge.maintenance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.upload.path")
public class FileUploadProperties {

    private Path editorImages;
    private Path attachments;
    private Path profileImage;

    public Path getEditorImages() { return editorImages; }
    public void setEditorImages(Path editorImages) { this.editorImages = editorImages; }

    public Path getAttachments() { return attachments; }
    public void setAttachments(Path attachments) { this.attachments = attachments; }

    public Path getProfileImage() { return profileImage; }
    public void setProfileImage(Path profileImage) { this.profileImage = profileImage; }

    public static class Path {
        private String baseDir;

        public String getBaseDir() { return baseDir; }
        public void setBaseDir(String baseDir) { this.baseDir = baseDir; }
    }
}
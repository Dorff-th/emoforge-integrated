package dev.emoforge.attach.policy;

import dev.emoforge.attach.domain.UploadType;

import java.util.List;

public interface UploadPolicy {
    UploadType getType();
    String getBaseDir();
    String getPublicUrl(String fileName);
    long getMaxSize();
    List<String> allowedExtensions();
}
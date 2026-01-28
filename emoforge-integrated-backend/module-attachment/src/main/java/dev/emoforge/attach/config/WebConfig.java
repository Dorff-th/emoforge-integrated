package dev.emoforge.attach.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path.profile-image.base-dir}")
    private String profileImageBaseDir;

    @Value("${file.upload.path.profile-image.public-url}")
    private String profileImagePublicUrl;

    @Value("${file.upload.path.editor-images.base-dir}")
    private String imageBaseDir;

    @Value("${file.upload.path.editor-images.public-url}")
    private String imagePublicUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 🧱 에디터 이미지 (예: /uploads/editor_images/**)
        registry.addResourceHandler(imagePublicUrl + "**")
                .addResourceLocations("file:" + ensureTrailingSlash(imageBaseDir));

        // 🧱 프로필 이미지 (예: /uploads/profile_image/**)
        registry.addResourceHandler(profileImagePublicUrl + "**")
                .addResourceLocations("file:" + ensureTrailingSlash(profileImageBaseDir));
    }

    /**
     * 경로 마지막에 '/' 없을 때 자동으로 추가해줌
     */
    private String ensureTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }
}



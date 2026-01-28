package dev.emoforge.attach.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Dev 환경에서만 `/api/attach/uploads/**` 경로를 정적 파일(`/uploads/**`)로 매핑하는 설정(이미지 url에만 해당).
 *
 * 운영(prod)에서는 Nginx가 rewrite(`/api/attach/uploads → /uploads`)를 수행하지만,
 * 개발(dev)에서는 해당 레이어가 없어 직접 ResourceHandler로 보정해주는 역할을 한다.
 *
 * 이를 통해 운영/개발 환경 모두 동일한 public URL 방식을 유지할 수 있다.
 */
@Configuration
@Profile("dev")
public class WebConfigDev implements WebMvcConfigurer {

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

        // ▶ dev에서만 /api/attach/uploads/** → /uploads/** 동일하게 제공
        // 🧱 에디터 이미지 (예: /uploads/editor_images/**)
        registry.addResourceHandler("api/attach/uploads/images/**")
                .addResourceLocations("file:" + ensureTrailingSlash(imageBaseDir));

        // 🧱 프로필 이미지 (예: /uploads/profile_image/**)
        registry.addResourceHandler("api/attach/uploads/profile_image/**")
                .addResourceLocations("file:" + ensureTrailingSlash(profileImageBaseDir));
    }

    private String ensureTrailingSlash(String path) {
        return path.endsWith("/") ? path : path + "/";
    }
}

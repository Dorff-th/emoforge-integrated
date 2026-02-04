package dev.emoforge.app.security.endpoint;

public final class AttachEndpoints {

    private AttachEndpoints() {}

    public static final String[] ATTACH_PUBLIC_ENDPOINTS = {
            "/uploads/**",
            "/api/attach/uploads/**",
            "/api/attach/download/**",
            "/api/attach/post/**",
            "/api/attach/profile/**",
            "/api/attach/profile-images/**",
            "/api/attach/public/profile",
            "/api/attach/count/**",
            "/api/attach/welcome",
            "/api/attach/test/**"
    };

    public static final String[] ATTACH_AUTHENTICATED_GET_ENDPOINTS = {
            "/api/attach/me/statistics"
    };

    public static final String[] ATTACH_AUTHENTICATED_POST_ENDPOINTS = {
            "/api/attach/**"
    };

    public static final String[] ATTACH_AUTHENTICATED_ENDPOINTS = {
            "/api/attach/**"
    };
}

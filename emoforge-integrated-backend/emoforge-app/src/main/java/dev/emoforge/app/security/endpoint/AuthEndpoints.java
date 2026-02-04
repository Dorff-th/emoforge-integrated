package dev.emoforge.app.security.endpoint;

public final class AuthEndpoints {

    private AuthEndpoints() {}

    public static final String[] AUTH_PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/logout",
            "/api/auth/refresh",
            "/api/auth/health",
            "/api/auth/public/**",
            "/api/auth/kakao",
            "/api/auth/kakao/signup",
            "/api/auth/admin/login"
    };

    public static final String[] AUTH_ADMIN_ENDPOINTS = {
            "/api/auth/admin/**"
    };

    public static final String[] AUTH_AUTHENTICATED_ENDPOINTS = {
            "/api/auth/**"
    };
}

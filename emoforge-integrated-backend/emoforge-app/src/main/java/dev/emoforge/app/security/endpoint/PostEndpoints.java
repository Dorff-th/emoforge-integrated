package dev.emoforge.app.security.endpoint;

public class PostEndpoints {
    private PostEndpoints() {}

    public static final String[] POST_PUBLIC_GET_ENDPOINTS = {
            "/api/posts/**"
    };

    public static final String[] POST_AUTHENTICATED_GET_ENDPOINTS = {
            "/api/posts/me/statistics"
    };

    public static final String[] POST_AUTHENTICATED_POST_ENDPOINTS = {
            "/api/posts/**"
    };

    public static final String[] POST_AUTHENTICATED_PUT_ENDPOINTS = {
            "/api/posts/**"
    };

    public static final String[] POST_AUTHENTICATED_DELETE_ENDPOINTS = {
            "/api/posts/**"
    };



    public static final String[] POST_ADMIN_ENDPOINTS = {
            "/api/posts/admin/**"
    };



}

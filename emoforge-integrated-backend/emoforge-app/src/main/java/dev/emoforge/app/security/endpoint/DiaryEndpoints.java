package dev.emoforge.app.security.endpoint;

public class DiaryEndpoints {
    private DiaryEndpoints() {}

    public static final String[] DIARY_PUBLIC_ENDPOINTS = {
            "/api/diary/welcome/**"
    };

    public static final String[] DIARY_AUTHENTICATED_ENDPOINTS = {
            "/api/diary/**"
    };
}

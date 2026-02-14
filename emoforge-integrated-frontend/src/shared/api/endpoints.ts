export const API = {
  AUTH: "/api/auth",
  POST: "/api/posts",
  DIARY: "/api/diary",
  ATTACH: "/api/attach",

  ADMIN: {
    AUTH: "/api/auth/admin",
    POST: "/api/posts/admin",
    DIARY: "/api/diary/admin",
  },
} as const;

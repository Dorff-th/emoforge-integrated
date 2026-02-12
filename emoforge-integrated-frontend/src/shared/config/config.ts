// src/config.ts
export const backendApiAttachUrl = import.meta.env.VITE_API_BASE_URL;
// ex: http://localhost:8080/api

// 정적 리소스 (이미지, 첨부파일 등) 접근용
export const backendBaseUrl = backendApiAttachUrl.replace(/\/api$/, '');
// ex: http://localhost:8080

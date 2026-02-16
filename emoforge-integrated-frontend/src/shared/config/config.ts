// src/config.ts
export const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;
export const backendApiAttachUrl = apiBaseUrl + "/api/attach";


// 정적 리소스 (이미지, 첨부파일 등) 접근용
export const backendBaseUrl = backendApiAttachUrl.replace(/\/api$/, '');
// ex: http://localhost:8080

export const serverBaseUrl = apiBaseUrl.replace(/\/api$/, '');
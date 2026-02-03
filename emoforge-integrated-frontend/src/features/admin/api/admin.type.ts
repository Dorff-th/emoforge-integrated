export interface AdminLoginRequest {
    username: string;
    password: string;
    captchaToken: string;
  
}

export interface AdminInfo {
  nickname: string;
  role: string;
  message: string;
}

export const adminAuthKeys = {
  me: ['admin', 'me'] as const,
};
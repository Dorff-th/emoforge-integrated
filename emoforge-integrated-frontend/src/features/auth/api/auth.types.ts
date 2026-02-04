// auth.types.ts
export interface AuthUser {
  uuid: string;
  username: string;
  role: "USER" | "ADMIN";
}

export type AuthErrorCode =
  | "inactive"
  | "deleted"
  | "unauthorized";


/**
 * 서버에서 내려주는 최소 사용자 정보
 * (FE에서 신뢰 가능한 인증 기준)
 */
export interface AuthMeResponse {
  uuid: string;
  username: string;
  nickname: string;
  email: string | null;
  role: "USER" | "ADMIN";
}

/**
 * 로그인 요청 payload
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * 카카오 로그인 응답 Response Type
 */
export interface KakaoLoginResponse {
  status: 'LOGIN_OK' | 'NEED_AGREEMENT';
  kakaoId: string | null;
  nickname: string | null;
}  

export interface KakaoSignupRequest {
  kakaoId: string;
  nickname: string;
}
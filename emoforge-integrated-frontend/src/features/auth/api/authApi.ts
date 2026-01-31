import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

/**
 * 서버에서 내려주는 최소 사용자 정보
 * (FE에서 신뢰 가능한 인증 기준)
 */
export interface AuthMeResponse {
  uuid: string;
  username: string;
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
 * Auth API – "백엔드와의 계약서"
 */
export const authApi = {

   kakaoLogin: (code: string) =>
    http.post<void>(`${API.AUTH}/kakao`,{code}),

  /**
   * 로그인
   * - 성공: refresh_token 쿠키 설정
   * - 실패: 401 throw
   */
  login: (payload: LoginRequest) =>
    http.post<void>(`${API.AUTH}/login`, payload),

  /**
   * 로그아웃
   * - refresh_token 쿠키 제거
   */
  logout: () =>
    http.post<void>(`${API.AUTH}/logout`),

  /**
   * 내 정보 조회
   * - 인증 실패 시 401
   * - refresh 실패 이후에도 여기서 401 발생
   */
  me: () =>
    http.get<AuthMeResponse>(`${API.AUTH}/me`),
};

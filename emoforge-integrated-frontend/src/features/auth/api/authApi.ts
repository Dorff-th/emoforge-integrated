import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { AuthMeResponse, LoginRequest, KakaoLoginResponse, KakaoSignupRequest } from "./auth.types";



/**
 * Auth API – "백엔드와의 계약서"
 */
export const authApi = {

   kakaoLogin: (code: string) =>
    http.post<KakaoLoginResponse>(`${API.AUTH}/kakao`,{code}),

  /**
   * 로그인
   * - 성공: refresh_token 쿠키 설정
   * - 실패: 401 throw
   */
  login: (payload: LoginRequest) =>
    http.post<void>(`${API.AUTH}/login`, payload),

  /**
   * 카카오 id 로 회원가입
   * -
   */
  kakaoSignup: ({ kakaoId, nickname }: KakaoSignupRequest) =>
  http.post<void>(`${API.AUTH}/kakao/signup`, {
    kakaoId: Number(kakaoId), // 🔥 경계에서 단 1회 변환
    nickname,
  }),
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

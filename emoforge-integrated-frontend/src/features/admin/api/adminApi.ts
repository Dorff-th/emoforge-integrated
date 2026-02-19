import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { AdminLoginRequest, AdminInfo } from "./admin.type";


/**
 * Admin API – "백엔드와의 계약서"
 */
export const adminApi = {
  /**
   * 관리자 로그인
   * - 성공: refresh_token 쿠키 설정
   * - 실패: 401 throw
   */
  adminLogin: async (payload: AdminLoginRequest): Promise<void> => {
    
    await http.post(`${API.ADMIN.AUTH}/login`, payload);
  },

  /**
   * 관리자 로그아웃
   * - access_token
   * - refresh_token 쿠키 제거
   */
  adminLogout: async (): Promise<void> => {
    await http.post(`${API.ADMIN.AUTH}/logout`);
  },

  /**
   * 관리자 정보 조회
   * - 인증 실패 시 401
   * - refresh 실패 이후에도 여기서 401 발생
   */
  adminMe: async (): Promise<AdminInfo> => {
    const res = await http.get<AdminInfo>(`${API.ADMIN.AUTH}/me`);
    return res.data; // 🔥 핵심
  },
};



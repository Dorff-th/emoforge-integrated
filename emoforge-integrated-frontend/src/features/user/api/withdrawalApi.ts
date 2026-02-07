import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

/**
 * BE Controller
 *  - MemberWithdrawalController
 *  - Base: /api/auth/me
 * Endpoints
 *  - POST /withdrawal
 *  - POST /withdrawal/cancel
 */

const MEMBER_WITHDRAWAL = `${API.AUTH}/me/withdrawal`;

/**
 * 회원 탈퇴 요청
 */
export const requestWithdrawal = () => http.post(`${MEMBER_WITHDRAWAL}`);

/**
 * 회원 탈퇴 취소
 */
export const requestWithdrawalCancle = () => http.post(`${MEMBER_WITHDRAWAL}/cancel`);


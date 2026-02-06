import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

/**
 * BE Controller
 * - MemberProfileController
 * - Base: /api/auth/members
 *
 * Endpoints
 * - GET  /check-nickname
 * - PUT  /nickname
 * - GET  /check-email
 * - PUT  /email
 */
const MEMBER_PROFILE = `${API.AUTH}/members`;

export const checkNickname = (nickname: string) =>
  http.get(`${MEMBER_PROFILE}/check-nickname`, { params: { nickname } });

export const updateNickname = (nickname: string) =>
  http.put(`${MEMBER_PROFILE}/nickname`, { nickname });

export const checkEmail = (email: string) =>
  http.get(`${MEMBER_PROFILE}/check-email`, { params: { email } });

export const updateEmail = (email: string) =>
  http.put(`${MEMBER_PROFILE}/email`, { email });

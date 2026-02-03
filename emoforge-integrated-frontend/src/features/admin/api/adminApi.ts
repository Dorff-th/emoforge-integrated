import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { AdminLoginRequest } from "./admin.type";


/**
 * Admin API – "백엔드와의 계약서"
 */
export const adminApi = {
    adminLogin : (payload : AdminLoginRequest) =>
        http.post<void>(`${API.ADMIN}/login`, payload),

};
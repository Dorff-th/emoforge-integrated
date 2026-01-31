import axios, { AxiosError, type AxiosInstance } from "axios";
import { OAuthFlow } from "@/features/auth/api/authFlow";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const http: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  timeout: 10_000,      // 최신 JS에소 3자리씩 구분법(, 쓰면 안됨)
});

/**
 * refresh 동시성 제어용
 */
let isRefreshing = false;
let refreshQueue: (() => void)[] = [];

function subscribeTokenRefresh(cb: () => void) {
  refreshQueue.push(cb);
}

function onRefreshed() {
  refreshQueue.forEach(cb => cb());
  refreshQueue = [];
}

/**
 * Response Interceptor
 */
http.interceptors.response.use(
  res => res,
  async (error: AxiosError) => {
    const status = error.response?.status;
    const originalRequest: any = error.config;
    

    // 🔒 OAuth 진행 중이면 401도 그냥 통과
    if (status === 401 && OAuthFlow.isActive()) {
      return Promise.reject(error);
    }

    // 401 아니면 패스
    if (status !== 401) {
      return Promise.reject(error);
    }

    // refresh 자체의 401은 더 이상 확산 ❌
    if (originalRequest?.url?.includes("/api/auth/refresh")) {
      return Promise.reject(error);
    }

    // retry 중복 방지
    if (originalRequest._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    // refresh 중이면 큐 대기
    if (isRefreshing) {
      await new Promise<void>(resolve => subscribeTokenRefresh(resolve));
      return http(originalRequest);
    }

    isRefreshing = true;

    try {
      await axios.post(
        `${BASE_URL}/api/auth/refresh`,
        null,
        { withCredentials: true }
      );

      onRefreshed();
      return http(originalRequest);

    } finally {
      isRefreshing = false;
    }
  }
);




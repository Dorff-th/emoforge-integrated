import axios, { AxiosError, type AxiosInstance } from "axios";

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
  response => response,
  async (error: AxiosError) => {
    const status = error.response?.status;
    const originalRequest: any = error.config;

    // 401이 아니면 그대로 에러 전달
    if (status !== 401) {
      return Promise.reject(error);
    }

    // refresh API 자체에서 401 → 상위에서 logout 판단
    if (originalRequest?.url?.includes("/api/auth/refresh")) {
      return Promise.reject(error);
    }

    // 이미 retry 한 요청이면 더 이상 시도 X
    if (originalRequest._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    // 이미 refresh 중이면 큐에 대기
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

    } catch (refreshError) {
      // refresh 실패 → 판단은 상위 레이어 몫
      return Promise.reject(refreshError);

    } finally {
      isRefreshing = false;
    }
  }
);

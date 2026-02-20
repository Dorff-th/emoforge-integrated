import axios, { AxiosError, type AxiosInstance } from "axios";
import { OAuthFlow } from "@/features/auth/api/authFlow";
import { useLoadingStore } from "@/shared/stores/loadingStore";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;
export const SKIP_LOADING_HEADER = "X-Skip-Loading";

export const http: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  timeout: 10_000,
});

/**
 * refresh 동시성 제어
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
 * Request Interceptor
 * - 기본적으로 전역 로딩 ON
 * - X-Skip-Loading 헤더가 있으면 제외
 */
http.interceptors.request.use(
  config => {
    const skip = config.headers?.[SKIP_LOADING_HEADER] === "1";
    if (!skip) {
      useLoadingStore.getState().start();
    }
    return config;
  },
  error => {
    // 요청 단계 에러 방어
    useLoadingStore.getState().end();
    return Promise.reject(error);
  }
);

/**
 * Response Interceptor
 */
http.interceptors.response.use(
  res => {
    const skip = res.config.headers?.[SKIP_LOADING_HEADER] === "1";
    if (!skip) {
      useLoadingStore.getState().end();
    }
    return res;
  },
  async (error: AxiosError) => {
    const status = error.response?.status;
    const originalRequest: any = error.config;

    const skip = originalRequest?.headers?.[SKIP_LOADING_HEADER] === "1";
    if (!skip) {
      useLoadingStore.getState().end();
    }

    /**
     * 🔒 OAuth 진행 중이면 401도 그대로 통과
     */
    if (status === 401 && OAuthFlow.isActive()) {
      return Promise.reject(error);
    }

    /**
     * 401 아니면 그대로 throw
     */
    if (status !== 401) {
      return Promise.reject(error);
    }

    /**
     * refresh API 자체가 401이면 확산 ❌
     */
    if (originalRequest?.url?.includes("/api/auth/refresh")) {
      return Promise.reject(error);
    }

    /**
     * retry 중복 방지
     */
    if (originalRequest._retry) {
      return Promise.reject(error);
    }
    originalRequest._retry = true;

    /**
     * 이미 refresh 중이면 큐에서 대기
     */
    if (isRefreshing) {
      await new Promise<void>(resolve => subscribeTokenRefresh(resolve));
      return http(originalRequest);
    }

    isRefreshing = true;

    try {
      /**
       * refresh 요청은
       * - 전역 로딩 제외
       * - axios 인스턴스 재사용 ❌ (인터셉터 재귀 방지)
       */
      await axios.post(
        `${BASE_URL}/api/auth/refresh`,
        null,
        {
          withCredentials: true,
          headers: { [SKIP_LOADING_HEADER]: "1" },
        }
      );

      onRefreshed();
      return http(originalRequest);

    } finally {
      isRefreshing = false;
    }
  }
);

/**
 * helper
 * 사용 예:
 * http.get("/health", { headers: skipLoading() })
 */
export const skipLoading = () =>
  ({ [SKIP_LOADING_HEADER]: "1" } as const);

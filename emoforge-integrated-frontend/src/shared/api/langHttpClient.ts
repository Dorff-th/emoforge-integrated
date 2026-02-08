import axios, { AxiosError, type AxiosInstance } from "axios";
import { useLoadingStore } from "@/shared/stores/loadingStore";

const LANG_BASE_URL = import.meta.env.VITE_API_LANGGRAPH_BASE_URL;
const SKIP_LOADING_HEADER = "X-Skip-Loading";

export const langHttp: AxiosInstance = axios.create({
  baseURL: LANG_BASE_URL,
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



/**
 * Request Interceptor
 * - 기본적으로 전역 로딩 ON
 * - X-Skip-Loading 헤더가 있으면 제외
 */
langHttp.interceptors.request.use(
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
langHttp.interceptors.response.use(
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
      return langHttp(originalRequest);
    }

    

    
  }
);
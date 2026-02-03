import { useEffect } from "react";
import { useLoadingStore } from "@/shared/stores/loadingStore";

type UILoadingOptions = {
  /**
   * UI 로딩 지속 시간 (ms)
   * - 기본값: 250ms
   * - enter 연출용
   */
  duration?: number;

  /**
   * 컴포넌트 unmount 시 end 호출 여부
   * - 기본값: true
   * - route 이동 / 조건부 렌더링 대비
   */
  endOnUnmount?: boolean;
};

/**
 * UI 전용 Loading Hook
 *
 * - 네트워크 로딩 ❌ (axios interceptor 담당)
 * - 화면 전환 / 컴포넌트 진입 연출 ⭕
 *
 * 사용 예:
 *   useUILoading("posts:list");
 *   useUILoading("admin:auth-check", { duration: 300 });
 */
export function useUILoading(
  scope: string,
  options: UILoadingOptions = {}
) {
  const { duration = 250, endOnUnmount = true } = options;
  const loading = useLoadingStore();

  useEffect(() => {
    // start UI loading
    loading.start(scope);

    const timer = window.setTimeout(() => {
      loading.end(scope);
    }, duration);

    return () => {
      window.clearTimeout(timer);

      if (endOnUnmount) {
        loading.end(scope);
      }
    };
    // scope가 바뀌면 새로운 UI 로딩으로 간주
  }, [scope, duration, endOnUnmount]);
}

import { useEffect, useRef, useState } from "react";
import { useLoadingStore } from "@/shared/stores/loadingStore";

const SHOW_DELAY_MS = 200;
const MIN_SHOW_MS = 300;

export function GlobalLoadingOverlay() {
  const isGlobalLoading = useLoadingStore((s) => s.isGlobalLoading());
  const [visible, setVisible] = useState(false);

  const showTimer = useRef<number | null>(null);
  const shownAt = useRef<number | null>(null);
  const hideTimer = useRef<number | null>(null);

  useEffect(() => {
    // 로딩 시작
    if (isGlobalLoading) {
      if (hideTimer.current) window.clearTimeout(hideTimer.current);

      if (!visible) {
        showTimer.current = window.setTimeout(() => {
          shownAt.current = Date.now();
          setVisible(true);
        }, SHOW_DELAY_MS);
      }
      return;
    }

    // 로딩 종료
    if (showTimer.current) {
      window.clearTimeout(showTimer.current);
      showTimer.current = null;
    }

    if (visible) {
      const elapsed = shownAt.current ? Date.now() - shownAt.current : 0;
      const remain = Math.max(0, MIN_SHOW_MS - elapsed);

      hideTimer.current = window.setTimeout(() => {
        setVisible(false);
        shownAt.current = null;
      }, remain);
    }
  }, [isGlobalLoading, visible]);

  if (!visible) return null;

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-black/40 backdrop-blur-sm">
      <div className="flex flex-col items-center">
        <div className="h-12 w-12 animate-spin rounded-full border-4 border-t-transparent border-white" />
        <p className="mt-3 text-white">Loading...</p>
      </div>
    </div>
  );
}

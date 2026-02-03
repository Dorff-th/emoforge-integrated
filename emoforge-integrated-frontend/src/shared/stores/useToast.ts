// src/shared/stores/useToast.ts
import { useToastStore } from "./toastStore";

export function useToast() {
  const { add, remove, clear } = useToastStore();

  return {
    success: (message: string, duration?: number) =>
      add({
        type: "success",
        message,
        duration,
      }),

    error: (message: string, duration?: number) =>
      add({
        type: "error",
        message,
        duration: duration ?? 5000, // 에러는 기본 길게
      }),

    info: (message: string, duration?: number) =>
      add({
        type: "info",
        message,
        duration,
      }),

    remove: (id: string) => remove(id),
    clear,
  };
}

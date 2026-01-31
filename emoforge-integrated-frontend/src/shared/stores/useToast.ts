// src/shared/stores/useToast.ts
import { useToastStore } from "./toastStore";

export function useToast() {
  const { add, remove, clear } = useToastStore();

  return {
    success: (message: string) =>
      add({ type: "success", message }),

    error: (message: string) =>
      add({ type: "error", message }),

    info: (message: string) =>
      add({ type: "info", message }),

    remove,
    clear,
  };
}

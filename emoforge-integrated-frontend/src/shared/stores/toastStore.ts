import { create } from "zustand";
import type { Toast, ToastPayload } from "./toast.types";
import { generateId } from '@/shared/utils/generateId';


interface ToastState {
  toasts: Toast[];
  add: (toast: ToastPayload) => void;   // ✅ 여기 중요!
  remove: (id: string) => void;
  clear: () => void;
  pause: (id: string) => void;
  resume: (id: string) => void;
}

export const useToastStore = create<ToastState>((set, get) => {

  const startTick = (id: string) => {
    const tick = () => {
      const t = get().toasts.find((t) => t.id === id);
      if (!t) return;

      if (t.paused) {
        requestAnimationFrame(tick); // 🔥 paused여도 루프 유지
        return;
      }

      const elapsed = Date.now() - t.createdAt;
      const remaining = t.duration - elapsed;

      if (remaining <= 0) {
        set((state) => ({
          toasts: state.toasts.filter((x) => x.id !== id),
        }));
        return;
      }

      set((state) => ({
        toasts: state.toasts.map((x) =>
          x.id === id ? { ...x, remaining } : x
        ),
      }));

      requestAnimationFrame(tick);
    };

    requestAnimationFrame(tick);
  };

  return {
    toasts: [],

    add: (toast) => {
      const id = generateId();
      const duration = toast.duration ?? 3000;
      const now = Date.now();

      const newToast: Toast = {
        id,
        type: toast.type,
        message: toast.message,
        duration,
        createdAt: now,
        remaining: duration,
        paused: false,
      };

      set((state) => ({
        toasts: [...state.toasts, newToast],
      }));

      startTick(id); // ✅ 여기서 시작
    },

    pause: (id: string) =>
      set((state) => ({
        toasts: state.toasts.map((t) =>
          t.id === id
            ? {
                ...t,
                paused: true,
                duration: t.remaining,
                createdAt: Date.now(),
              }
            : t
        ),
      })),

    resume: (id: string) => {
      set((state) => ({
        toasts: state.toasts.map((t) =>
          t.id === id
            ? {
                ...t,
                paused: false,
                createdAt: Date.now(),
              }
            : t
        ),
      }));

      startTick(id); // ✅ resume 시 재시동
    },

    remove: (id: string) =>
      set((state) => ({
        toasts: state.toasts.filter((t) => t.id !== id),
      })),

    clear: () => set({ toasts: [] }),
  };
});



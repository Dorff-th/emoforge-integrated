import { create } from "zustand";
import { type Toast } from "./toast.types";
import { generateId } from '@/shared/utils/generateId';

interface ToastState {
  toasts: Toast[];
  add: (toast: Omit<Toast, "id">) => void;
  remove: (id: string) => void;
  clear: () => void;
}

export const useToastStore = create<ToastState>((set) => ({
  toasts: [],

  add: (toast) =>
  set((state) => ({
    toasts: [
      ...state.toasts,
      { ...toast, id: generateId() },
    ],
  })),

  remove: (id) =>
    set((state) => ({
      toasts: state.toasts.filter((t) => t.id !== id),
    })),

  clear: () =>
    set({ toasts: [] }),
}));

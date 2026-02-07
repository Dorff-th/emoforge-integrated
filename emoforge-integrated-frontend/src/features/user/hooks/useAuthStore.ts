// stores/useAuthStore.ts
import { create } from "zustand";

interface AuthState {
  user: any | null;
  isAuthenticated: boolean;
  clearAuth: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,

  clearAuth: () =>
    set({
      user: null,
      isAuthenticated: false,
    }),
}));

import { create } from "zustand";

type ScopeKey = string;

type LoadingState = {
  globalCount: number;
  scopes: Record<ScopeKey, number>;

  start: (scope?: ScopeKey) => void;
  end: (scope?: ScopeKey) => void;

  isLoading: (scope?: ScopeKey) => boolean;
  isGlobalLoading: () => boolean;
};

export const useLoadingStore = create<LoadingState>((set, get) => ({
  globalCount: 0,
  scopes: {},

  start: (scope) => {
     console.log("[UI LOADING START]", scope);
    set((s) => {
      if (!scope) return { globalCount: s.globalCount + 1 };
      const next = (s.scopes[scope] ?? 0) + 1;
      return { scopes: { ...s.scopes, [scope]: next } };
    });
  },

  end: (scope) => {
    console.log("[UI LOADING END]", scope);
    set((s) => {
      if (!scope) return { globalCount: Math.max(0, s.globalCount - 1) };

      const curr = s.scopes[scope] ?? 0;
      const next = Math.max(0, curr - 1);

      // 0이면 키 삭제 (깔끔)
      const scopes = { ...s.scopes };
      if (next === 0) delete scopes[scope];
      else scopes[scope] = next;

      return { scopes };
    });
  },

  isLoading: (scope) => {
    const s = get();
    if (!scope) return s.globalCount > 0;
    return (s.scopes[scope] ?? 0) > 0;
  },

  isGlobalLoading: () => get().globalCount > 0,
}));

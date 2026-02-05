// stores/themeStore.ts
import { create } from "zustand";

type ThemeMode = "light" | "dark";

interface ThemeState {
  theme: ThemeMode;
  toggle: () => void;
  setTheme: (theme: ThemeMode) => void;
}

export const useThemeStore = create<ThemeState>((set) => ({
  theme: "light",

  toggle: () =>
    set((state) => {
      const next = state.theme === "light" ? "dark" : "light";
      document.documentElement.classList.toggle("dark", next === "dark");
      localStorage.setItem("theme", next);
      return { theme: next };
    }),

  setTheme: (theme) =>
    set(() => {
      document.documentElement.classList.toggle("dark", theme === "dark");
      localStorage.setItem("theme", theme);
      return { theme };
    }),
}));

// hooks/useInitTheme.ts
import { useEffect } from "react";
import { useThemeStore } from "@/shared/stores/themeStore";

export function useInitTheme() {
  const setTheme = useThemeStore((s) => s.setTheme);

  useEffect(() => {
    const stored = localStorage.getItem("theme");
    if (stored === "dark" || stored === "light") {
      setTheme(stored);
    }
  }, [setTheme]);
}

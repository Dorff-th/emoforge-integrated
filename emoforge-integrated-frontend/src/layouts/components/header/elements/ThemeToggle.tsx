// components/ThemeToggle.tsx
import { useThemeStore } from "@/shared/stores/themeStore";
import { Sun, Moon } from "lucide-react";

export function ThemeToggle() {
  const { theme, toggle } = useThemeStore();

  return (
    <button
      onClick={toggle}
      aria-label="Toggle theme"
      className="
        rounded-lg
        p-2
        bg-[var(--surface)]
        text-[var(--text)]
        hover:bg-[var(--border)]
        transition
        flex items-center gap-1.5
      "
    >
      {theme === "dark" ? (
        <Sun size={18} strokeWidth={1.75} />
      ) : (
        <Moon size={18} strokeWidth={1.75} />
      )}
    </button>
  );
}

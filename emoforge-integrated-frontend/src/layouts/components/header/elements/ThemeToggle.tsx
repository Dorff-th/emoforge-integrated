// components/ThemeToggle.tsx
import { useThemeStore } from "@/shared/stores/themeStore";

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
      "
    >
      {theme === "dark" ? "☀️" : "🌙"}
    </button>
  );
}

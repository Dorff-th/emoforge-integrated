// src/layouts/PublicLayout.tsx
import { Outlet } from "react-router-dom";
import { AppHeader } from "@/layouts/components/header/AppHeader";
import { HeaderContextProvider } from "@/layouts/components/header/context/headerContext";

export function PublicLayout() {
  return (
    <HeaderContextProvider value="PUBLIC">
      <div className="min-h-screen bg-[var(--bg)] text-[var(--text)] transition-colors">
        <AppHeader />
        <main className="mx-auto max-w-7xl px-4 py-8 bg-[var(--surface)]">
          <Outlet />
        </main>
      </div>
    </HeaderContextProvider>
  );
}

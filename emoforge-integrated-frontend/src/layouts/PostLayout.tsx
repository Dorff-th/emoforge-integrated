import { Outlet } from "react-router-dom";
import { AppHeader } from "@/layouts/components/header/AppHeader";
import { HeaderContextProvider } from "@/layouts/components/header/context/headerContext";

export function PostLayout() {
  return (
    <HeaderContextProvider value="POST">
      <div className="min-h-screen bg-white">
        <AppHeader />

        <main className="mx-auto max-w-7xl px-4 py-8">
          <Outlet />
        </main>
      </div>
    </HeaderContextProvider>
  );
}

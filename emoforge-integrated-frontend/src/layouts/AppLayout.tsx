import { Outlet } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";

export function AppLayout() {
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 flex flex-col">
      {/* Header */}
      <header className="sticky top-0 z-40 bg-white border-b border-slate-200">
        <div className="mx-auto max-w-6xl px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-slate-900 text-white font-bold">
              EF
            </div>
            <span className="font-semibold tracking-tight">Emoforge</span>
          </div>

          <div className="flex items-center gap-4">
            {user && (
              <span className="text-sm text-slate-600">
                {user.data?.username}
              </span>
            )}
            <button
              type="button"
              onClick={logout}
              className="rounded-md px-3 py-1.5 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              로그아웃
            </button>
          </div>
        </div>
      </header>

      {/* Main */}
      <main className="flex-1 mx-auto w-full max-w-6xl px-4 py-6">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-200 bg-white">
        <div className="mx-auto max-w-6xl px-4 py-3 text-xs text-slate-400">
          © {new Date().getFullYear()} Emoforge
        </div>
      </footer>
    </div>
  );
}

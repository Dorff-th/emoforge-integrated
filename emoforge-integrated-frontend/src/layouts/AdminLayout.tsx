import { Outlet } from "react-router-dom";
import { RequireAdmin } from "@/guards/RequireAdmin";
import { useAdminAuth } from "@/features/admin/hooks/useAdminAuth";

export function AdminLayout() {
  const { adminInfo, adminLogout, isLoggingOut } = useAdminAuth();

  return (
    <RequireAdmin>
      <div className="min-h-screen">
        <header className="flex items-center justify-between px-6 py-4 border-b">
          <div className="font-bold">Admin</div>

          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">
              {adminInfo?.nickname ?? "관리자"}
            </span>

            <button
              onClick={() => adminLogout()}
              disabled={isLoggingOut}
              className="text-sm text-red-600 hover:underline disabled:opacity-50"
            >
              {isLoggingOut ? "로그아웃 중..." : "로그아웃"}
            </button>
          </div>
        </header>

        <main className="p-6">
          <Outlet />
        </main>
      </div>
    </RequireAdmin>
  );
}

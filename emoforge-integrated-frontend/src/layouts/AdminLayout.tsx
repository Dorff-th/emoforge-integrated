import { Outlet } from "react-router-dom";
import { RequireAdmin } from "@/guards/RequireAdmin";
import AdminHeader from "@/layouts/AdminHeader";
import AdminSidebar from "@/layouts/AdminSidebar";

export function AdminLayout() {
  return (
    <RequireAdmin>
      <div className="flex min-h-screen bg-gray-100">
        {/* Sidebar */}
        <AdminSidebar />

        {/* Main Area */}
        <div className="flex flex-col flex-1">
          <AdminHeader />
          <main className="flex-1 p-6 overflow-y-auto">
            <Outlet />
          </main>
        </div>
      </div>
    </RequireAdmin>
  );
}

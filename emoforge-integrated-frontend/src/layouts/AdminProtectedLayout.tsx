import { RequireAdmin } from "@/guards/RequireAdmin";
import { AdminLayout } from "@/layouts/AdminLayout";

export function AdminProtectedLayout() {
  return (
    <RequireAdmin>
      <AdminLayout />
    </RequireAdmin>
  );
}

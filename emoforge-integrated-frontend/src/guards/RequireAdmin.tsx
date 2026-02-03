import { type ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAdminAuth } from "@/features/admin/hooks/useAdminAuth";

export function RequireAdmin({ children }: { children: ReactNode }) {
  const { isAuthorized, isLoading } = useAdminAuth();

  if (isLoading) {
    return <div>Loading...</div>; // TODO: Admin 전용 로딩 UI
  }

  if (!isAuthorized) {
    return <Navigate to="/admin/login" replace />;
  }

  return children;
}

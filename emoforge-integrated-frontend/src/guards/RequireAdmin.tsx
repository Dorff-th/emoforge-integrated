import { type ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAdminAuth } from "@/features/admin/hooks/useAdminAuth";
import { AdminAuthLoading } from "@/features/admin/components/AdminAuthLoading";

export function RequireAdmin({ children }: { children: ReactNode }) {
  const { isAuthorized, isLoading } = useAdminAuth();

  if (isLoading) {
    return <AdminAuthLoading />;
  }

  if (!isAuthorized) {
    return <Navigate to="/admin/login" replace />;
  }

  return children;
}


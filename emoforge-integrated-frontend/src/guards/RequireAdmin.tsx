// src/guards/RequireAdmin.tsx
import { Navigate, Outlet } from "react-router-dom";

export function RequireAdmin() {
  // TODO: admin auth check
  const isAdmin = true; // placeholder

  if (!isAdmin) {
    return <Navigate to="/admin/login" replace />;
  }

  return <Outlet />;
}

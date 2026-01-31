// src/layouts/PublicLayout.tsx
import { Outlet } from "react-router-dom";
import { AuthErrorHandler } from "./components/AuthErrorHandler";

export function PublicLayout() {
  return (
    <>
      <AuthErrorHandler />
      <Outlet />
    </>
  );
}

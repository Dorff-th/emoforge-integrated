// src/layouts/AdminLayout.tsx
import { Outlet } from "react-router-dom";

export function AdminLayout() {
  return (
    <div>
      <header>Admin Header (TODO)</header>
      <main>
        <Outlet />
      </main>
    </div>
  );
}

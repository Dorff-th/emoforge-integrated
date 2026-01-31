import { RouterProvider } from "react-router-dom";
import { QueryProvider } from "./providers/QueryProvider";
import { router } from "@/app/router";
import { ToastHost } from "@/shared/components/ToastHost";

export function App() {
  return (
    <QueryProvider>
      <ToastHost />
      <RouterProvider router={router} />
    </QueryProvider>
  );
}

import { RouterProvider } from "react-router-dom";
import { QueryProvider } from "./providers/QueryProvider";
import { router } from "@/app/router";
import { ToastHost } from "@/shared/components/ToastHost";
import { GlobalLoadingOverlay } from "@/shared/components/GlobalLoadingOverlay";

export function App() {
  return (
    <QueryProvider>
      <GlobalLoadingOverlay />
      <ToastHost />
      <RouterProvider router={router} />
    </QueryProvider>
  );
}

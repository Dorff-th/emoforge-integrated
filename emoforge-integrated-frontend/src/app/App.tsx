import { RouterProvider } from "react-router-dom";
import { QueryProvider } from "./providers/QueryProvider";
import { router } from "@/app/router";
import { ToastHost } from "@/shared/components/ToastHost";
import { GlobalLoadingOverlay } from "@/shared/components/GlobalLoadingOverlay";
import { useInitTheme } from "./providers/useInitTheme";

export function App() {
  useInitTheme();
  return (
    <QueryProvider>
      <GlobalLoadingOverlay />
      <ToastHost />
      <RouterProvider router={router} />
    </QueryProvider>
  );
}

import { createBrowserRouter, Navigate } from "react-router-dom";
import { PublicLayout } from "@/layouts/PublicLayout";
import { AppLayout } from "@/layouts/AppLayout";
import { RequireAuth } from "@/guards/RequireAuth";
import { LoginPage } from "@/features/auth/pages/LoginPage";
import { OAuthCallbackPage } from "@/features/auth/pages/OAuthCallbackPage";
import { HomePage } from "@/features/auth/pages/HomePage";
import TermsAgreementPage from "@/features/auth/pages/TermsAgreementPage";

export const router = createBrowserRouter([
  {
    element: <PublicLayout />,
    children: [
      { path: "/login", element: <LoginPage /> },
      { path: "/auth/terms", element: <TermsAgreementPage /> },
      { path: "/kakao/callback", element: <OAuthCallbackPage /> },
    ],
  },
  {
    path: "/", // ✅ 명시
    element: <RequireAuth />,
    children: [
      {
        element: <AppLayout />,
        children: [{ index: true, element: <HomePage /> }],
      },
    ],
  },
  {
    path: "*",
    element: <Navigate to="/login" replace />,
  },
]);

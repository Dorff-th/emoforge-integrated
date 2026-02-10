import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate, Outlet } from "react-router-dom";
import { PublicLayout } from "@/layouts/PublicLayout";
import { PostLayout } from "@/layouts/PostLayout";
import { UserLayout } from "@/layouts/UserLayout ";
import { RequireAuth } from "@/guards/RequireAuth";
import { SectionLoading } from "@/shared/components/SectionLoading";
//import { AdminLayout } from "@/layouts/AdminLayout";
import { AdminProtectedLayout } from "@/layouts/AdminProtectedLayout";
import LoginPage from "@/features/auth/pages/LoginPage";
import OAuthCallbackPage from "@/features/auth/pages/OAuthCallbackPage";
import UserHomePage from "@/features/user/pages/UserHomePage";
import ProfilePage from "@/features/user/pages/ProfilePage";
import PostWritePage from "@/features/post/pages/PostWritePage";
import PostEditPage from "@/features/post/pages/PostEditPage";
import DiaryWritePage from "@/features/diary/pages/DiaryWritePage";

import TermsAgreementPage from "@/features/auth/pages/TermsAgreementPage";
import DiaryListPage from "@/features/diary/pages/DiaryListPage";

import PostListPage from "@/features/post/pages/PostListPage";
import PostDetailPage from "@/features/post/pages/PostDetailPage";
import AdminDashboardPage from "@/features/admin/pages/AdminDashboardPage";
import AdminMembersPage from "@/features/admin/pages/AdminMembersPage";
import AdminPostCategoryPage from "@/features/admin/pages/AdminDashboardPage";
import AdminLoginPage from "@/features/admin/pages/AdminLoginPage";
import WithdrawalPendingPage from "@/features/user/pages/WithdrawalPendingPage";

const CalendarPage = lazy(
  () => import("@/features/calendar/pages/CalendarPage"),
);

const DiaryInsightsPage = lazy(
  () => import("@/features/diary/pages/DiaryInsightsPage"),
);

const DiarySearchPage = lazy(
  () => import("@/features/diary/pages/DiarySearchPage"),
);

export const router = createBrowserRouter([
  {
    element: <PublicLayout />,
    children: [
      { path: "/login", element: <LoginPage /> },
      { path: "/auth/terms", element: <TermsAgreementPage /> },
      { path: "/kakao/callback", element: <OAuthCallbackPage /> },
      { path: "/admin/login", element: <AdminLoginPage /> },
      { path: "/withdraw/pending", element: <WithdrawalPendingPage /> },
    ],
  },
  {
    path: "/posts",
    element: <PostLayout />,
    children: [
      { index: true, element: <PostListPage /> },
      { path: "new", element: <Navigate to="/user/posts/new" replace /> },
      { path: ":id", element: <PostDetailPage /> },
    ],
  },

  {
    path: "/user",
    element: <RequireAuth />,
    children: [
      {
        element: <UserLayout />,
        children: [
          { index: true, element: <Navigate to="/user/home" replace /> },
          { path: "home", element: <UserHomePage /> },
          { path: "profile", element: <ProfilePage /> },

          // 🔒 게시글 작성/수정
          { path: "posts/new", element: <PostWritePage /> },
          { path: "posts/:id/edit", element: <PostEditPage /> },

          // 🔒 다이어리 전부
          { path: "diary/write", element: <DiaryWritePage /> },
          { path: "diary/list", element: <DiaryListPage /> },
          // 🔒 다이어리 - lazy 묶음
          {
            element: (
              <Suspense fallback={<SectionLoading scope="route:diary" />}>
                <Outlet />
              </Suspense>
            ),
            children: [
              { path: "diary/calendar", element: <CalendarPage /> },
              { path: "diary/insights", element: <DiaryInsightsPage /> },
              { path: "diary/search", element: <DiarySearchPage /> },
            ],
          },
        ],
      },
    ],
  },
  {
    path: "/admin",
    element: <AdminProtectedLayout />,
    children: [
      { index: true, element: <Navigate to="dashboard" replace /> },
      { path: "dashboard", element: <AdminDashboardPage /> },
      { path: "members", element: <AdminMembersPage /> },
      { path: "posts/categories", element: <AdminPostCategoryPage /> },
    ],
  },
  {
    path: "/",
    element: <Navigate to="/posts" replace />,
  },
  { path: "*", element: <div>404 NOT FOUND</div> },
]);

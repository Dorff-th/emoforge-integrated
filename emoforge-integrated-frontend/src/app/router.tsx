import { createBrowserRouter, Navigate } from "react-router-dom";
import { PublicLayout } from "@/layouts/PublicLayout";
import { PostLayout } from "@/layouts/PostLayout";
import { UserLayout } from "@/layouts/UserLayout ";
import { RequireAuth } from "@/guards/RequireAuth";
//import { AdminLayout } from "@/layouts/AdminLayout";
import { AdminProtectedLayout } from "@/layouts/AdminProtectedLayout";
import LoginPage from "@/features/auth/pages/LoginPage";
import OAuthCallbackPage from "@/features/auth/pages/OAuthCallbackPage";
import UserHomePage from "@/features/user/pages/UserHomePage";
import ProfilePage from "@/features/user/pages/ProfilePage";
import PostWritePage from "@/features/post/pages/PostWritePage";
import PostEditPage from "@/features/post/pages/PostEditPage";
import DiaryWritePage from "@/features/diary/pages/DiaryWritePage";
import DiaryCalendarPage from "@/features/diary/pages/DiaryCalendarPage";
import TermsAgreementPage from "@/features/auth/pages/TermsAgreementPage";
import DiaryListPage from "@/features/diary/pages/DiaryListPage";
import DiaryInsightsPage from "@/features/diary/pages/DiaryInsightsPage";
import DiarySearchPage from "@/features/diary/pages/DiarySearchPage";
import PostListPage from "@/features/post/pages/PostListPage";
import PostDetailPage from "@/features/post/pages/PostDetailPage";
import AdminDashboardPage from "@/features/admin/pages/AdminDashboardPage";
import AdminMembersPage from "@/features/admin/pages/AdminMembersPage";
import AdminPostCategoryPage from "@/features/admin/pages/AdminDashboardPage";
import AdminLoginPage from "@/features/admin/pages/AdminLoginPage";
import WithdrawalPendingPage from "@/features/user/pages/WithdrawalPendingPage";

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
          { path: "diary/calendar", element: <DiaryCalendarPage /> },
          { path: "diary/list", element: <DiaryListPage /> },
          { path: "diary/insights", element: <DiaryInsightsPage /> },
          { path: "diary/search", element: <DiarySearchPage /> },
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

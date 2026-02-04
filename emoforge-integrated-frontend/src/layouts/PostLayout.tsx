import { Outlet } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { PostHeader } from "./components/header/PostHeader";

export const PostLayout = () => {
  const { isAuthenticated } = useAuth();

  return (
    <>
      <PostHeader isAuthenticated={isAuthenticated} />
      <Outlet />
    </>
  );
};

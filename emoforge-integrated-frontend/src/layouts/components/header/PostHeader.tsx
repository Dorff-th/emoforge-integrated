import { AppHeader } from "./AppHeader";
//import { useAuth } from "@/features/auth/hooks/useAuth";

export const PostHeader = ({
  isAuthenticated,
}: {
  isAuthenticated: boolean;
}) => {
  return (
    <AppHeader
      left={<div>EmoForge</div>}
      center={<a href="/posts">Posts</a>}
      right={
        isAuthenticated ? (
          <a href="/user/home">My</a>
        ) : (
          <a href="/login">Login</a>
        )
      }
    />
  );
};

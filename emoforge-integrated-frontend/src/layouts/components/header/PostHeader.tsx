import { useNavigate, useLocation, Link } from "react-router-dom";
import { AppHeader } from "./AppHeader";
//import { useAuth } from "@/features/auth/hooks/useAuth";

export const PostHeader = ({
  isAuthenticated,
}: {
  isAuthenticated: boolean;
}) => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate("/login", {
      state: { from: location.pathname },
    });
  };

  return (
    <AppHeader
      left={<div>EmoForge</div>}
      center={<a href="/posts">Posts</a>}
      right={
        isAuthenticated ? (
          <Link to="/user/home">My</Link>
        ) : (
          <div>
            <button onClick={handleLogin}>Login</button>
          </div>
        )
      }
    />
  );
};

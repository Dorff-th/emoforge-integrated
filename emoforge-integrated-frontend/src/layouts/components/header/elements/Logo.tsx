import { Link } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";

export function Logo() {
  const { isAuthenticated } = useAuth();

  return (
    <Link
      to={isAuthenticated ? "/user/home" : "/posts"}
      className="font-semibold text-[var(--text)]"
    >
      EmoForge
    </Link>
  );
}

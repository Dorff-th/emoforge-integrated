import { Link } from "react-router-dom";
import { LogIn } from "lucide-react";

export function LoginButton() {
  return (
    <div>
      <Link
        to="/login"
        className="text-sm text-[var(--text)] opacity-80 hover:opacity-100 flex items-center gap-1.5"
      >
        <LogIn size={16} />
        Login
      </Link>
    </div>
  );
}

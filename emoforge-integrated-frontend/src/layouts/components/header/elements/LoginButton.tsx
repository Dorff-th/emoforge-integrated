import { Link } from "react-router-dom";
export function LoginButton() {
  return (
    <div>
      <Link
        to="/login"
        className="
  text-sm
  text-[var(--text)]
  opacity-80
  hover:opacity-100
"
      >
        Login
      </Link>
    </div>
  );
}

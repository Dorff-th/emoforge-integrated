import { Link } from "react-router-dom";
export function LoginButton() {
  return (
    <div>
      <Link to="/login" className="text-xl font-bold text-gray-800">
        Login
      </Link>
    </div>
  );
}

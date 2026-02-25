import { Home, LogIn } from "lucide-react";
import { Link, NavLink, Outlet } from "react-router-dom";

export default function AboutLayout() {
  return (
    <div className="max-w-6xl mx-auto px-6 py-20">
      {/* Title Row */}
      <div className="flex items-center justify-between mb-10">
        {/* Left 영역 */}
        <div className="flex items-center gap-4">
          <Link
            to="/posts"
            className="text-muted-foreground hover:text-foreground transition hover:scale-110"
          >
            <Home size={22} />
          </Link>

          <h1 className="text-4xl font-bold">About Emoforge</h1>
        </div>

        {/* Right 영역 - Login 버튼 */}
        <Link
          to="/login"
          className="px-5 py-2 rounded-lg border border-primary text-primary flex items-center gap-1.5 hover:bg-primary hover:text-white transition"
        >
          <LogIn size={16} />
          Login
        </Link>
      </div>

      {/* Tabs */}
      <nav className="flex gap-8 border-b pb-4 mb-12">
        <NavLink
          to="intro"
          className={({ isActive }) =>
            isActive
              ? "font-semibold border-b-2 border-primary pb-1"
              : "text-muted-foreground hover:text-foreground"
          }
        >
          Intro
        </NavLink>

        <NavLink
          to="portfolio"
          className={({ isActive }) =>
            isActive
              ? "font-semibold border-b-2 border-primary pb-1"
              : "text-muted-foreground hover:text-foreground"
          }
        >
          Portfolio
        </NavLink>
      </nav>

      <Outlet />
    </div>
  );
}

import { NavLink, useNavigate, Link } from "react-router-dom";
import { Pencil, Search } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";

export function PostHeader() {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleWriteClick = () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }
    navigate("/posts/new");
  };

  return (
    <div className="flex w-full items-center justify-between">
      {/* Left */}
      <div className="flex items-center gap-4">
        <NavLink
          to="/posts"
          className={({ isActive }) =>
            `text-sm transition-colors hover:text-black ${
              isActive ? "text-black font-medium" : "text-neutral-600"
            }`
          }
        >
          Posts
        </NavLink>
      </div>

      {/* Center (Desktop only - Search placeholder) */}
      <div className="hidden md:flex flex-1 justify-center">
        <button
          disabled
          className="flex w-64 items-center gap-2 rounded-md border px-3 py-1.5 text-sm text-neutral-400"
        >
          <Search size={14} />
          검색 (준비 중)
        </button>
      </div>

      {/* Right */}
      <div className="flex items-center gap-2">
        {/* Mobile search */}
        <button className="md:hidden p-2 text-neutral-600">
          <Search size={18} />
        </button>

        {/* Write */}
        <button
          onClick={handleWriteClick}
          className="flex items-center gap-1 rounded-md border px-3 py-1.5 text-sm hover:bg-neutral-100 md:px-4"
        >
          <Pencil size={16} />
          <span className="hidden md:inline">글쓰기</span>
        </button>
      </div>
    </div>
  );
}

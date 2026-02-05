import { NavLink, useNavigate } from "react-router-dom";
import { Pencil, Search } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { cn } from "@/shared/utils/cn";

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
    <div className="flex w-full items-center justify-between text-[var(--text)]">
      {/* Left */}
      <NavLink
        to="/posts"
        className={({ isActive }) =>
          cn(
            "text-sm transition-colors",
            isActive
              ? "font-medium text-[var(--text)]"
              : "text-[var(--text)] hover:text-[var(--text)]",
          )
        }
      >
        Posts
      </NavLink>

      {/* Center (Desktop only - Search placeholder) */}
      <div className="hidden md:flex flex-1 justify-center">
        <button
          disabled
          className="flex w-64 items-center gap-2 rounded-md border border-[var(--border)] px-3 py-1.5 text-sm text-[var(--muted)] bg-[var(--surface)]"
        >
          <Search size={14} />
          검색 (준비 중)
        </button>
      </div>

      {/* Right */}
      <div className="flex items-center gap-2">
        {/* Mobile search */}
        <button className="md:hidden p-2 text-[var(--muted)] hover:text-[var(--text)]">
          <Search size={18} />
        </button>

        {/* Write */}
        <button
          onClick={handleWriteClick}
          className="flex items-center gap-1 rounded-md border border-[var(--border)] px-3 py-1.5 md:px-4 text-sm text-[var(--text)] bg-[var(--surface)] hover:bg-[var(--border)]"
        >
          <Pencil size={16} />
          <span className="hidden md:inline">글쓰기</span>
        </button>
      </div>
    </div>
  );
}

import { useNavigate } from "react-router-dom";
import { Pencil, Search, FileText } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { HeaderNavItem } from "../elements/HeaderNavItem";
import { PostSearchInput } from "../elements/PostSearchInput";

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
      <HeaderNavItem to="/posts" icon={FileText}>
        Posts
      </HeaderNavItem>

      {/* Center (Desktop only - Search placeholder) */}

      <PostSearchInput />

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
          <span className="hidden md:inline">Write</span>
        </button>
      </div>
    </div>
  );
}

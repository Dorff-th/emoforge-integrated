import { useNavigate } from "react-router-dom";
import { Pencil, FileText } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { HeaderNavItem } from "../elements/HeaderNavItem";
import { PostSearchInput } from "../elements/PostSearchInput";

interface PostHeaderProps {
  mobileSearchOnly?: boolean;
}

export function PostHeader({ mobileSearchOnly = false }: PostHeaderProps) {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleWriteClick = () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }
    navigate("/posts/new");
  };

  if (mobileSearchOnly) {
    return (
      <div className="md:hidden">
        <PostSearchInput />
      </div>
    );
  }

  return (
    <div className="flex w-full min-w-0 items-center justify-end text-[var(--text)] md:justify-between">
      {/* Left */}
      <div className="hidden md:block">
        <HeaderNavItem to="/posts" icon={FileText}>
          Posts
        </HeaderNavItem>
      </div>

      {/* Desktop Search */}
      <div className="hidden min-w-0 flex-1 justify-center px-6 md:flex">
        <div className="w-full max-w-md">
          <PostSearchInput />
        </div>
      </div>

      {/* Right */}
      <div className="flex shrink-0 items-center gap-2">
        <button
          onClick={handleWriteClick}
          className="flex items-center gap-1 rounded-md border border-[var(--border)] bg-[var(--surface)] px-3 py-1.5 text-sm text-[var(--text)] hover:bg-[var(--border)] md:px-4"
        >
          <Pencil size={16} />
          <span className="hidden md:inline">Write</span>
        </button>
      </div>
    </div>
  );
}

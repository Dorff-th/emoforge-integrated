import { ChevronDown } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { cn } from "@/shared/utils/cn";

interface Props {
  open: boolean;
  onToggle: () => void;
}

export function ProfileTrigger({ open, onToggle }: Props) {
  const { user } = useAuth();

  return (
    <button
      onClick={onToggle}
      className="
      flex items-center gap-2
      rounded-full
      px-2 py-1
      bg-[var(--surface)]
      text-[var(--text)]
      border border-[var(--border)]
      hover:bg-[var(--border)]
      transition
    "
    >
      {/* Avatar */}
      <div
        className="
        flex h-8 w-8 items-center justify-center
        rounded-full
        bg-[var(--border)]
        text-sm font-semibold
        text-[var(--text)]
      "
      >
        {user?.data.nickname?.[0] ?? "?"}
      </div>

      {/* Nickname */}
      <span className="max-w-[100px] truncate text-sm">
        {user?.data.nickname}
      </span>

      {/* Chevron */}
      <ChevronDown
        size={16}
        className={cn("transition-transform", open && "rotate-180")}
      />
    </button>
  );
}

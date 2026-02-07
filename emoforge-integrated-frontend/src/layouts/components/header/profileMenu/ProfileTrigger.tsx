import { ChevronDown } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { cn } from "@/shared/utils/cn";
import { useProfileImage } from "@/features/user/hooks/useProfileImage";
import { Avatar } from "@/shared/components/Avatar";

interface Props {
  open: boolean;
  onToggle: () => void;
}

export function ProfileTrigger({ open, onToggle }: Props) {
  const { user } = useAuth();
  const { publicUrl } = useProfileImage(user?.uuid);

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
      <Avatar
        publicUrl={publicUrl}
        name={user?.nickname}
        size={32}
        className="text-sm"
      />

      {/* Nickname */}
      <span className="max-w-[100px] truncate text-sm">{user?.nickname}</span>

      {/* Chevron */}
      <ChevronDown
        size={16}
        className={cn("transition-transform", open && "rotate-180")}
      />
    </button>
  );
}

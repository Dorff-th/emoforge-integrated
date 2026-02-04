import { ChevronDown } from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";

interface Props {
  open: boolean;
  onToggle: () => void;
}

export function ProfileTrigger({ open, onToggle }: Props) {
  const { user } = useAuth();

  return (
    <button
      onClick={onToggle}
      className="flex items-center gap-2 rounded-full px-2 py-1 hover:bg-neutral-100"
    >
      <div className="h-8 w-8 rounded-full bg-neutral-300 flex items-center justify-center text-sm font-semibold">
        {user?.data.nickname?.[0] ?? "?"}
      </div>

      <span className="max-w-[100px] truncate text-sm">
        {user?.data.nickname}
      </span>

      <ChevronDown
        size={16}
        className={`transition-transform ${open ? "rotate-180" : ""}`}
      />
    </button>
  );
}

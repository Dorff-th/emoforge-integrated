import { useAuth } from "@/features/auth/hooks/useAuth";
import { User, Mail } from "lucide-react";

export function ProfileSummary() {
  const { user } = useAuth();

  return (
    <div className="px-4 py-3">
      <div className="text-sm font-medium flex items-center gap-1.5">
        <User size={16} />
        {user?.nickname}
      </div>
      <div className="text-xs text-neutral-500 flex items-center gap-1.5">
        <Mail size={16} />
        {user?.email}
      </div>
    </div>
  );
}

import { useAuth } from "@/features/auth/hooks/useAuth";

export function ProfileSummary() {
  const { user } = useAuth();

  console.log(user?.data.email);

  return (
    <div className="px-4 py-3">
      <div className="text-sm font-medium">{user?.data.nickname}</div>
      <div className="text-xs text-neutral-500">{user?.data.email}</div>
    </div>
  );
}

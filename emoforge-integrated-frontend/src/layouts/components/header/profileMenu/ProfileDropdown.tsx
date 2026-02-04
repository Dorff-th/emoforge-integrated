import { ProfileSummary } from "./ProfileSummary";
import { ProfileMenuItem } from "./ProfileMenuItem";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useNavigate } from "react-router-dom";

interface Props {
  onClose: () => void;
}

export function ProfileDropdown({ onClose }: Props) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    onClose();
  };

  return (
    <div className="absolute right-0 mt-2 w-56 rounded-md border bg-white shadow-md">
      <ProfileSummary />

      <div className="py-1">
        <ProfileMenuItem
          label="프로필 설정"
          onClick={() => navigate("/user/profile")}
        />
        <ProfileMenuItem
          label="내 글 / 회고"
          onClick={() => navigate("/user/diary/list")}
        />
        <ProfileMenuItem label="대시보드" onClick={() => navigate("/user")} />
      </div>

      <div className="border-t py-1">
        <ProfileMenuItem
          label="로그아웃"
          variant="danger"
          onClick={handleLogout}
        />
      </div>
    </div>
  );
}

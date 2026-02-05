import { ProfileSummary } from "./ProfileSummary";
import { ProfileMenuItem } from "./ProfileMenuItem";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { Settings, LogOut, BookOpen, LayoutDashboard } from "lucide-react";

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
          label="Profile Setting"
          onClick={() => navigate("/user/profile")}
          icon={Settings}
        />
        <ProfileMenuItem
          label="Emotion & Diary"
          onClick={() => navigate("/user/diary/list")}
          icon={BookOpen}
        />
        <ProfileMenuItem
          label="DashBoard"
          onClick={() => navigate("/user")}
          icon={LayoutDashboard}
        />
      </div>

      <div className="border-t py-1">
        <ProfileMenuItem
          label="Logout"
          variant="danger"
          onClick={handleLogout}
          icon={LogOut}
        />
      </div>
    </div>
  );
}

import { useHeaderContext } from "@/layouts/components/hooks/useHeaderContext";
import { PublicHeader } from "@/layouts/components/header/context/PublicHeader";
import { PostHeader } from "@/layouts/components/header/context/PostHeader";
import { UserHeader } from "@/layouts/components/header/context/UserHeader";
import { DiaryHeader } from "@/layouts/components/header/context/DiaryHeader";
import { AdminHeader } from "@/layouts/components/header/context/AdminHeader";

export function AppHeader() {
  const context = useHeaderContext();

  return (
    <header className="sticky top-0 z-50 border-b bg-white">
      <div className="mx-auto flex h-14 max-w-7xl items-center px-4">
        {/* Left */}
        <div className="flex items-center gap-4">
          <Logo />
        </div>

        {/* Center */}
        <div className="flex flex-1 justify-center">
          {context === "PUBLIC" && <PublicHeader />}
          {context === "POST" && <PostHeader />}
          {context === "USER" && <UserHeader />}
          {context === "DIARY" && <DiaryHeader />}
          {context === "ADMIN" && <AdminHeader />}
        </div>

        {/* Right */}
        <div className="flex items-center gap-3">
          <ThemeToggle />
          <ProfileMenu />
        </div>
      </div>
    </header>
  );
}

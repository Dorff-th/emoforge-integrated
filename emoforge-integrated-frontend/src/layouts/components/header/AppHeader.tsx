import { useHeaderContext } from "@/layouts/components/hooks/useHeaderContext";
import { PublicHeader } from "@/layouts/components/header/context/PublicHeader";
import { PostHeader } from "@/layouts/components/header/context/PostHeader";
import { UserHeader } from "@/layouts/components/header/context/UserHeader";
import { DiaryHeader } from "@/layouts/components/header/context/DiaryHeader";
import { Logo } from "@/layouts/components/header/elements/Logo";
import { AppHeaderRight } from "@/layouts/components/header/AppHeaderRight";
import { useHeaderScrolled } from "@/layouts/components/hooks/useHeaderScrolled";
import { cn } from "@/shared/utils/cn";

export function AppHeader() {
  const context = useHeaderContext();

  const scrolled = useHeaderScrolled();

  return (
    <header
      className={cn(
        "sticky top-0 z-50",
        "bg-background/80 backdrop-blur-md",
        "transition-shadow duration-150",
        scrolled && "shadow-sm",
      )}
    >
      <div className="mx-auto max-w-7xl px-4">
        <div className="flex h-14 items-center gap-3">
          {/* Left */}
          <div className="flex shrink-0 items-center gap-4">
            <Logo />
          </div>

          {/* Center */}
          <div className="flex min-w-0 flex-1 justify-center">
            {context === "PUBLIC" && <PublicHeader />}
            {context === "POST" && <PostHeader />}
            {context === "USER" && <UserHeader />}
            {context === "DIARY" && <DiaryHeader />}
          </div>

          {/* Right */}
          <div className="ml-auto shrink-0">
            <AppHeaderRight context={context} />
          </div>
        </div>

        {context === "POST" && (
          <div className="border-t border-[var(--border)] py-2 md:hidden">
            <PostHeader mobileSearchOnly />
          </div>
        )}
      </div>
    </header>
  );
}

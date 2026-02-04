import { LoginButton } from "@/layouts/components/header/elements/LoginButton";
import { ProfileMenu } from "@/layouts/components/header/profileMenu/ProfileMenu";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { ThemeToggle } from "@/layouts/components/header/elements/ThemeToggle";

type HeaderContext = "PUBLIC" | "POST" | "USER" | "DIARY";

interface Props {
  context: HeaderContext;
}

export function AppHeaderRight({ context }: Props) {
  const { isAuthenticated } = useAuth();

  const showLoginButton =
    context === "PUBLIC" || (context === "POST" && !isAuthenticated);

  const showProfileMenu =
    (context === "POST" && isAuthenticated) ||
    context === "USER" ||
    context === "DIARY";

  return (
    <div className="flex items-center gap-3">
      <ThemeToggle />

      {showLoginButton && <LoginButton />}
      {showProfileMenu && <ProfileMenu />}
    </div>
  );
}

import { NavLink } from "react-router-dom";
import { type LucideIcon } from "lucide-react";
import {
  HEADER_MENU_ITEM_BASE,
  HEADER_HOVER_TRANSITION,
  HEADER_UNDERLINE,
  HEADER_ICON_SIZE,
  HEADER_ICON_SIZE_MOBILE,
} from "@/layouts/components/header/header.constants";
import { cn } from "@/shared/utils/cn";

interface HeaderNavItemProps {
  to: string;
  icon: LucideIcon;
  children: React.ReactNode;
  compact?: boolean;
}

export function HeaderNavItem({
  to,
  icon: Icon,
  children,
  compact,
}: HeaderNavItemProps) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) =>
        cn(
          HEADER_MENU_ITEM_BASE,
          HEADER_HOVER_TRANSITION,
          HEADER_UNDERLINE,
          "relative focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
          compact && "px-2 gap-1 text-xs",
          isActive
            ? "text-foreground after:w-full"
            : "opacity-80 hover:opacity-100 hover:text-foreground after:w-0 hover:after:w-full",
        )
      }
    >
      <Icon size={compact ? HEADER_ICON_SIZE_MOBILE : HEADER_ICON_SIZE} />
      {children}
    </NavLink>
  );
}

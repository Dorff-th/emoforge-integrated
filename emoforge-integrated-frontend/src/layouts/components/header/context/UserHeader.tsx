import { NavLink, useLocation } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { useState } from "react";
import { cn } from "@/shared/utils/cn";
import { User, FileText } from "lucide-react";
import {
  HEADER_MENU_ITEM_BASE,
  HEADER_ICON_SIZE_MOBILE,
  HEADER_HOVER_TRANSITION,
  HEADER_HOVER_BG,
} from "@/layouts/components/header/header.constants";
import { HeaderNavItem } from "../elements/HeaderNavItem";

const menus = [
  { label: "Profile", to: "/user/profile", icon: User },
  { label: "Posts", to: "/posts", icon: FileText },
];

export function UserHeader() {
  const location = useLocation();
  const [open, setOpen] = useState(false);

  const current =
    menus.find((m) => location.pathname.startsWith(m.to))?.label ?? "User";

  return (
    <div className="relative flex w-full items-center justify-between">
      {/* Desktop */}
      <nav className="hidden md:flex items-center gap-6 text-sm text-[var(--text)]">
        {menus.map(({ label, to, icon }) => (
          <HeaderNavItem key={to} to={to} icon={icon}>
            {label}
          </HeaderNavItem>
        ))}
      </nav>

      {/* Mobile */}
      <div className="flex w-full items-center justify-between md:hidden">
        <button
          onClick={() => setOpen((v) => !v)}
          className="p-2"
          aria-label="Open menu"
        >
          {open ? <X size={20} /> : <Menu size={20} />}
        </button>

        <span className="text-sm font-medium">{current}</span>

        {/* placeholder to balance layout */}
        <div className="w-6" />
      </div>

      {/* Mobile Menu */}
      {open && (
        <div className="absolute left-0 top-full z-50 w-full border-t bg-white shadow-md md:hidden">
          {menus.map(({ label, to, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              onClick={() => setOpen(false)}
              className={cn(
                HEADER_MENU_ITEM_BASE,
                HEADER_HOVER_TRANSITION,
                HEADER_HOVER_BG,
              )}
            >
              <Icon size={HEADER_ICON_SIZE_MOBILE} />
              {label}
            </NavLink>
          ))}
        </div>
      )}
    </div>
  );
}

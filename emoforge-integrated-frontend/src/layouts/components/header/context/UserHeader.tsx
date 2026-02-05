import { NavLink, useLocation } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { useState } from "react";
import { cn } from "@/shared/utils/cn";
import { User, FileText } from "lucide-react";
import {
  NAV_ITEM_BASE,
  HEADER_ICON_SIZE,
  HEADER_ICON_SIZE_MOBILE,
} from "@/layouts/components/header/header.constants";

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
        {menus.map(({ label, to, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              cn(
                NAV_ITEM_BASE,
                "text-foreground",
                isActive ? "font-medium" : "opacity-80 hover:opacity-100",
              )
            }
          >
            <Icon size={HEADER_ICON_SIZE} />
            {label}
          </NavLink>
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
              className="px-4 py-3 text-sm hover:bg-neutral-100 flex items-center gap-1.5"
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

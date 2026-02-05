import { NavLink } from "react-router-dom";
import { useState } from "react";
import { Menu, X, FileText, Layers } from "lucide-react";
import { cn } from "@/shared/utils/cn";
import {
  HEADER_MENU_ITEM_BASE,
  HEADER_ICON_SIZE_MOBILE,
  HEADER_HOVER_TRANSITION,
  HEADER_HOVER_BG,
} from "@/layouts/components/header/header.constants";
import { HeaderNavItem } from "../elements/HeaderNavItem";

export function PublicHeader() {
  const [open, setOpen] = useState(false);

  return (
    <nav className="flex items-center gap-6 text-[var(--text)]">
      {/* Desktop Menu */}
      <div className="hidden md:flex items-center gap-6 text-sm">
        <HeaderNavItem to="/about" icon={Layers}>
          About
        </HeaderNavItem>

        <HeaderNavItem to="/posts" icon={FileText}>
          Posts
        </HeaderNavItem>
      </div>

      {/* Mobile Hamburger */}
      <div className="md:hidden">
        <button
          onClick={() => setOpen((v) => !v)}
          aria-label="Open menu"
          className="p-2"
        >
          {open ? <X size={20} /> : <Menu size={20} />}
        </button>
      </div>

      {/* Mobile Dropdown */}
      {open && (
        <div className="absolute right-0 top-full mt-2 w-40 rounded-md border bg-white shadow-md md:hidden">
          <NavLink
            to="/about"
            onClick={() => setOpen(false)}
            className={cn(
              HEADER_MENU_ITEM_BASE,
              HEADER_HOVER_TRANSITION,
              HEADER_HOVER_BG,
            )}
          >
            <Layers size={HEADER_ICON_SIZE_MOBILE} />
            About
          </NavLink>

          <NavLink
            to="/posts"
            onClick={() => setOpen(false)}
            className={cn(
              HEADER_MENU_ITEM_BASE,
              HEADER_HOVER_TRANSITION,
              HEADER_HOVER_BG,
            )}
          >
            <FileText size={14} />
            Posts
          </NavLink>
        </div>
      )}
    </nav>
  );
}

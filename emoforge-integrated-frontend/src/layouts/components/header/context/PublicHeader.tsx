import { NavLink } from "react-router-dom";
import { useState } from "react";
import { Menu, X, FileText, Layers } from "lucide-react";
import { cn } from "@/shared/utils/cn";
import {
  NAV_ITEM_BASE,
  HEADER_ICON_SIZE,
  HEADER_ICON_SIZE_MOBILE,
} from "@/layouts/components/header/header.constants";

export function PublicHeader() {
  const [open, setOpen] = useState(false);

  return (
    <nav className="flex items-center gap-6 text-[var(--text)]">
      {/* Desktop Menu */}
      <div className="hidden md:flex items-center gap-6 text-sm">
        <NavLink
          to="/about"
          className={({ isActive }) =>
            cn(
              NAV_ITEM_BASE,
              "text-[var(--text)]",
              isActive ? "font-medium" : "opacity-80 hover:opacity-100",
            )
          }
        >
          <Layers size={HEADER_ICON_SIZE} />
          About
          {/* <ChevronDown size={14} /> */}
        </NavLink>

        <NavLink
          to="/posts"
          className={({ isActive }) =>
            cn(
              NAV_ITEM_BASE,
              "text-[var(--text)]",
              isActive ? "font-medium" : "opacity-80 hover:opacity-100",
            )
          }
        >
          <FileText size={HEADER_ICON_SIZE} />
          Posts
        </NavLink>
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
            className="px-4 py-2 text-sm hover:bg-neutral-100 flex items-center gap-1.5"
          >
            <Layers size={HEADER_ICON_SIZE_MOBILE} />
            About
          </NavLink>

          <NavLink
            to="/posts"
            onClick={() => setOpen(false)}
            className="px-4 py-2 text-sm hover:bg-neutral-100 flex items-center gap-1.5"
          >
            <FileText size={14} />
            Posts
          </NavLink>
        </div>
      )}
    </nav>
  );
}

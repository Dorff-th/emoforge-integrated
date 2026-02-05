import { NavLink } from "react-router-dom";
import { useState } from "react";
import { Menu, X, FileText, Layers } from "lucide-react";
import { cn } from "@/shared/utils/cn";

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
              "text-sm transition-colors text-[var(--text)] flex items-center gap-1.5",
              isActive ? "font-medium" : "opacity-80 hover:opacity-100",
            )
          }
        >
          <Layers size={16} />
          About
          {/* <ChevronDown size={14} /> */}
        </NavLink>

        <NavLink
          to="/posts"
          className={({ isActive }) =>
            cn(
              "text-sm transition-colors text-[var(--text)] flex items-center gap-1.5",
              isActive ? "font-medium" : "opacity-80 hover:opacity-100",
            )
          }
        >
          <FileText size={16} />
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
            <Layers size={14} />
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

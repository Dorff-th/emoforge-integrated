import { NavLink } from "react-router-dom";
import { useState } from "react";
import { Menu, X } from "lucide-react";

export function PublicHeader() {
  const [open, setOpen] = useState(false);

  return (
    <nav className="relative flex items-center">
      {/* Desktop Menu */}
      <div className="hidden md:flex items-center gap-6 text-sm">
        <NavLink
          to="/about"
          className={({ isActive }) =>
            `transition-colors hover:text-black ${
              isActive ? "text-black font-medium" : "text-neutral-600"
            }`
          }
        >
          About
        </NavLink>

        <NavLink
          to="/posts"
          className={({ isActive }) =>
            `transition-colors hover:text-black ${
              isActive ? "text-black font-medium" : "text-neutral-600"
            }`
          }
        >
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
        <div className="absolute right-0 top-full mt-2 w-40 rounded-md border bg-white shadow-md">
          <NavLink
            to="/about"
            onClick={() => setOpen(false)}
            className="block px-4 py-2 text-sm hover:bg-neutral-100"
          >
            About
          </NavLink>

          <NavLink
            to="/posts"
            onClick={() => setOpen(false)}
            className="block px-4 py-2 text-sm hover:bg-neutral-100"
          >
            Posts
          </NavLink>
        </div>
      )}
    </nav>
  );
}

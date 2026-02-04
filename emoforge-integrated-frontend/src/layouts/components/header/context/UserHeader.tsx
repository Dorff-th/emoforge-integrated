import { NavLink, useLocation } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { useState } from "react";

const menus = [
  { label: "Home", to: "/user/home" },
  { label: "Profile", to: "/user/profile" },
  { label: "Posts", to: "/posts" },
];

export function UserHeader() {
  const location = useLocation();
  const [open, setOpen] = useState(false);

  const current =
    menus.find((m) => location.pathname.startsWith(m.to))?.label ?? "User";

  return (
    <div className="relative flex w-full items-center justify-between">
      {/* Desktop */}
      <nav className="hidden md:flex items-center gap-6 text-sm">
        {menus.map((menu) => (
          <NavLink
            key={menu.to}
            to={menu.to}
            className={({ isActive }) =>
              `transition-colors hover:text-black ${
                isActive ? "text-black font-medium" : "text-neutral-600"
              }`
            }
          >
            {menu.label}
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
          {menus.map((menu) => (
            <NavLink
              key={menu.to}
              to={menu.to}
              onClick={() => setOpen(false)}
              className="block px-4 py-3 text-sm hover:bg-neutral-100"
            >
              {menu.label}
            </NavLink>
          ))}
        </div>
      )}
    </div>
  );
}

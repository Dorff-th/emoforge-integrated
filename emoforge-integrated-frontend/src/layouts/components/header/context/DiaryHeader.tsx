import { useState } from "react";
import { Menu, X, ExternalLink } from "lucide-react";
import { DiarySearchInput } from "@/features/diary/components/DiarySearchInput";
import { useDiarySearch } from "@/features/diary/search/useDiarySearch";
import { NavLink } from "react-router-dom";

const menus = [
  { label: "Write", to: "/user/diary/write" },
  { label: "Calendar", to: "/user/diary/calendar" },
  { label: "Entries", to: "/user/diary/list" },
  { label: "Analytics", to: "/user/diary/insights" },
];

// Diary → Board 이동용
const boardLink = { label: "Board", to: "/posts" };

export function DiaryHeader() {
  const { condition, setKeyword } = useDiarySearch();
  const [open, setOpen] = useState(false);

  return (
    <div className="relative flex w-full items-center justify-between gap-4">
      {/* ===== Left ===== */}

      {/* Desktop Nav */}
      <nav className="hidden md:flex items-center gap-4 text-sm">
        {menus.map((menu) => (
          <NavLink
            key={menu.to}
            to={menu.to}
            className={({ isActive }) =>
              `transition-colors hover:text-black ${
                isActive ? "font-medium text-black" : "text-neutral-600"
              }`
            }
          >
            {menu.label}
          </NavLink>
        ))}

        {/* Board link */}
        <NavLink
          to={boardLink.to}
          className="ml-2 flex items-center gap-1 text-neutral-500 hover:text-black"
        >
          {boardLink.label}
          <ExternalLink size={14} />
        </NavLink>
      </nav>

      {/* Mobile Hamburger */}
      <div className="md:hidden">
        <button
          onClick={() => setOpen((v) => !v)}
          className="p-2"
          aria-label="Open diary menu"
        >
          {open ? <X size={20} /> : <Menu size={20} />}
        </button>
      </div>

      {/* ===== Right: Search ===== */}
      <DiarySearchInput value={condition.keyword} onChange={setKeyword} />

      {/* ===== Mobile Dropdown ===== */}
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

          {/* Divider */}
          <div className="my-1 border-t" />

          <NavLink
            to={boardLink.to}
            onClick={() => setOpen(false)}
            className="flex items-center gap-2 px-4 py-3 text-sm text-neutral-600 hover:bg-neutral-100"
          >
            {boardLink.label}
            <ExternalLink size={14} />
          </NavLink>
        </div>
      )}
    </div>
  );
}

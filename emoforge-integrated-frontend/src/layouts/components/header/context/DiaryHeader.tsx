import { useState } from "react";
import {
  Menu,
  X,
  ExternalLink,
  PenLine,
  CalendarDays,
  List,
  BarChart3,
  FileText,
} from "lucide-react";
import { DiarySearchInput } from "@/features/diary/components/DiarySearchInput";
import { useDiarySearch } from "@/features/diary/search/useDiarySearch";
import { NavLink } from "react-router-dom";
import { cn } from "@/shared/utils/cn";
import {
  NAV_ITEM_BASE,
  HEADER_ICON_SIZE,
  HEADER_ICON_SIZE_MOBILE,
} from "@/layouts/components/header/header.constants";

const menus = [
  { label: "Write", to: "/user/diary/write", icon: PenLine },
  { label: "Calendar", to: "/user/diary/calendar", icon: CalendarDays },
  { label: "Entries", to: "/user/diary/list", icon: List },
  { label: "Analytics", to: "/user/diary/insights", icon: BarChart3 },
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
        {menus.map(({ label, to, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              cn(
                NAV_ITEM_BASE,
                "px-2 gap-1 text-xs",
                isActive ? "font-medium" : "opacity-80 hover:opacity-100",
              )
            }
          >
            <Icon size={HEADER_ICON_SIZE} />
            {label}
          </NavLink>
        ))}

        {/* Board link */}
        <NavLink
          to={boardLink.to}
          className="ml-2 flex items-center gap-1 text-neutral-500 hover:text-black"
        >
          <FileText size={HEADER_ICON_SIZE} />
          {boardLink.label}
          <ExternalLink size={HEADER_ICON_SIZE_MOBILE} />
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

          {/* Divider */}
          <div className="my-1 border-t" />

          <NavLink
            to={boardLink.to}
            onClick={() => setOpen(false)}
            className="flex items-center gap-2 px-4 py-3 text-sm text-neutral-600 hover:bg-neutral-100"
          >
            <FileText size={HEADER_ICON_SIZE_MOBILE} />
            {boardLink.label}
            <ExternalLink size={HEADER_ICON_SIZE_MOBILE} />
          </NavLink>
        </div>
      )}
    </div>
  );
}

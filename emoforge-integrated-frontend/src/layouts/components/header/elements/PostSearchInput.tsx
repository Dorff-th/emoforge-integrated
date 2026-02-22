import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search } from "lucide-react";

export function PostSearchInput() {
  const [searchQuery, setSearchQuery] = useState("");

  const inputRef = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      if (searchQuery.trim()) {
        navigate(
          `/posts/search/?keyword=${encodeURIComponent(
            searchQuery,
          )}&page=1&size=10`,
        );
      }
    }

    if (e.key === "Escape") {
      e.preventDefault();
      //onChange("");
      inputRef.current?.blur(); // Esc 키 누르면 input 에 포커스가 빠져나감
    }
  };

  return (
    <div className="hidden md:flex flex-1 justify-center">
      <div className="relative w-64">
        {/* 🔍 Icon */}
        <Search
          size={16}
          className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none"
        />

        <input
          ref={inputRef}
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Search posts…"
          className="
          w-full rounded-md border border-[var(--border)]
          bg-[var(--surface)]
          pl-9 pr-8 py-1.5 text-sm
          text-[var(--foreground)]
          focus:outline-none focus:ring-2 focus:ring-primary/40
        "
        />

        {/* ❌ Clear */}
        {searchQuery && (
          <button
            type="button"
            aria-label="검색어 지우기"
            onClick={() => setSearchQuery("")}
            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
          >
            ✕
          </button>
        )}
      </div>
    </div>
  );
}

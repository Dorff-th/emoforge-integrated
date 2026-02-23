import { Search } from "lucide-react";

interface Props {
  inputRef: React.RefObject<HTMLInputElement | null>;
  searchQuery: string;
  setSearchQuery: React.Dispatch<React.SetStateAction<string>>;
  handleKeyDown: (e: React.KeyboardEvent<HTMLInputElement>) => void;
}

export default function SearchInputUI({
  inputRef,
  searchQuery,
  setSearchQuery,
  handleKeyDown,
}: Props) {
  return (
    <div className="relative w-full max-w-xs">
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
  );
}

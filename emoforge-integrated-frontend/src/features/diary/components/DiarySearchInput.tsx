import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search } from "lucide-react";

type DiarySearchInputProps = {
  value: string;
  onChange: (value: string) => void;
  onSubmit?: () => void;
};

export function DiarySearchInput({ value, onChange }: DiarySearchInputProps) {
  const [searchQuery, setSearchQuery] = useState("");

  const inputRef = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      if (searchQuery.trim()) {
        navigate(
          `/user/diary/search/?query=${encodeURIComponent(
            searchQuery,
          )}&page=1&size=10`,
        );
      }
    }

    if (e.key === "Escape") {
      e.preventDefault();
      onChange("");
      inputRef.current?.blur(); // Esc 키 누르면 input 에 포커스가 빠져나감
    }
  };

  return (
    <div className="relative flex w-full max-w-sm items-center">
      {/* 🔍 Icon */}
      <span className="absolute left-3 text-gray-400">
        <Search size={16} />
      </span>

      <input
        ref={inputRef}
        type="text"
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="내 회고 검색…"
        className="
          w-full rounded-md border border-gray-300
          py-2 pl-9 pr-9 text-sm
          focus:border-gray-400 focus:outline-none
        "
      />

      {/* ❌ Clear */}
      {value && (
        <button
          type="button"
          aria-label="검색어 지우기"
          onClick={() => onChange("")}
          className="absolute right-3 text-gray-400 hover:text-gray-600"
        >
          ✕
        </button>
      )}
    </div>
  );
}

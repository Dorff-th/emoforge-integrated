import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import SearchInputUI from "./SearchInputUI";

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
    <SearchInputUI
      inputRef={inputRef}
      searchQuery={searchQuery}
      setSearchQuery={setSearchQuery}
      handleKeyDown={handleKeyDown}
    />
  );
}

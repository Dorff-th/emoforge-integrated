import type { AdminCommentSearchType } from "@/features/admin/api/adminPostApi";

type AdminCommentSearchBarProps = {
  searchType: AdminCommentSearchType;
  keyword: string;
  onSearchTypeChange: (searchType: AdminCommentSearchType) => void;
  onKeywordChange: (keyword: string) => void;
  onSearch: () => void;
};

export default function AdminCommentSearchBar({
  searchType,
  keyword,
  onSearchTypeChange,
  onKeywordChange,
  onSearch,
}: AdminCommentSearchBarProps) {
  return (
    <div className="mb-4 flex flex-wrap items-center gap-2">
      <select
        className="h-10 rounded border px-3 text-sm"
        value={searchType}
        onChange={(e) =>
          onSearchTypeChange(e.target.value as AdminCommentSearchType)
        }
      >
        <option value="ALL">전체</option>
        <option value="NICKNAME">작성자</option>
        <option value="COMMENT">댓글내용</option>
        <option value="POST">게시글제목</option>
      </select>
      <input
        type="text"
        className="h-10 w-64 rounded border px-3 text-sm"
        placeholder="검색어를 입력하세요"
        value={keyword}
        onChange={(e) => onKeywordChange(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            onSearch();
          }
        }}
      />
      <button
        type="button"
        className="h-10 rounded bg-gray-900 px-4 text-sm font-medium text-white"
        onClick={onSearch}
      >
        검색
      </button>
    </div>
  );
}

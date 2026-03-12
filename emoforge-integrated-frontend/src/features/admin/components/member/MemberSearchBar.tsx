export type MemberDeletedFilter = "ALL" | "ACTIVE" | "DELETED";

type MemberSearchBarProps = {
  nickname: string;
  deletedFilter: MemberDeletedFilter;
  onNicknameChange: (nickname: string) => void;
  onDeletedFilterChange: (deletedFilter: MemberDeletedFilter) => void;
  onSearch: () => void;
};

export default function MemberSearchBar({
  nickname,
  deletedFilter,
  onNicknameChange,
  onDeletedFilterChange,
  onSearch,
}: MemberSearchBarProps) {
  return (
    <div className="mb-4 rounded-lg border bg-white p-4 shadow-sm">
      <div className="flex flex-wrap items-end gap-3">
        <label className="flex min-w-[220px] flex-1 flex-col gap-1">
          <span className="text-sm font-medium text-gray-700">닉네임</span>
          <input
            type="text"
            value={nickname}
            onChange={(e) => onNicknameChange(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                onSearch();
              }
            }}
            placeholder="닉네임을 입력하세요"
            className="h-10 rounded border border-gray-300 px-3 text-sm outline-none transition focus:border-gray-900"
          />
        </label>

        <label className="flex w-40 flex-col gap-1">
          <span className="text-sm font-medium text-gray-700">회원 상태</span>
          <select
            value={deletedFilter}
            onChange={(e) =>
              onDeletedFilterChange(e.target.value as MemberDeletedFilter)
            }
            className="h-10 rounded border border-gray-300 px-3 text-sm outline-none transition focus:border-gray-900"
          >
            <option value="ALL">전체</option>
            <option value="ACTIVE">정상회원</option>
            <option value="DELETED">탈퇴회원</option>
          </select>
        </label>

        <button
          type="button"
          className="h-10 rounded bg-gray-900 px-4 text-sm font-medium text-white transition hover:bg-gray-700"
          onClick={onSearch}
        >
          검색
        </button>
      </div>
    </div>
  );
}

// diarySearch.types.ts
export type DiarySearchCondition = {
  keyword: string;
  from?: string; // YYYY-MM-DD
  to?: string;
  page: number;
  size: number;
};

export const DEFAULT_DIARY_SEARCH: DiarySearchCondition = {
  keyword: "",
  page: 1,
  size: 10,
};

export function buildDiarySearchParams(
  condition: DiarySearchCondition
): URLSearchParams {
  const params = new URLSearchParams();

  if (condition.keyword.trim()) {
    params.set("keyword", condition.keyword.trim());
  }

  if (condition.from) params.set("from", condition.from);
  if (condition.to) params.set("to", condition.to);

  if (condition.page > 1) {
    params.set("page", String(condition.page));
  }

  if (condition.size !== DEFAULT_DIARY_SEARCH.size) {
    params.set("size", String(condition.size));
  }

  return params;
}

export type UseDiarySearchResult = {
  condition: DiarySearchCondition;
  debouncedCondition: DiarySearchCondition;
  setKeyword: (keyword: string) => void;
  setPage: (page: number) => void;
};
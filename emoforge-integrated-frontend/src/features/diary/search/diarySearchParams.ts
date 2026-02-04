// diarySearchParams.ts
import type { DiarySearchCondition } from "./diarySearch.types";
import { DEFAULT_DIARY_SEARCH } from "./diarySearch.types";

export function parseDiarySearchParams(
  params: URLSearchParams
): DiarySearchCondition {
  const keyword = params.get("keyword") ?? "";

  const page = Math.max(
    Number(params.get("page")) || DEFAULT_DIARY_SEARCH.page,
    1
  );

  const size =
    Number(params.get("size")) || DEFAULT_DIARY_SEARCH.size;

  const from = params.get("from") || undefined;
  const to = params.get("to") || undefined;

  return {
    keyword,
    page,
    size,
    from,
    to,
  };
}

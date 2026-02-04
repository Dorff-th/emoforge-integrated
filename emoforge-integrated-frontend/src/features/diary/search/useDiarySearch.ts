// useDiarySearch.ts
import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import { useDebounce } from "@/shared/hooks/useDebounce"; // 이미 있거나 만들 예정
import { parseDiarySearchParams } from "./diarySearchParams";
import type { DiarySearchCondition, UseDiarySearchResult} from "./diarySearch.types";
import { buildDiarySearchParams }  from "./diarySearch.types";

export function useDiarySearch(): UseDiarySearchResult {
  const [searchParams, setSearchParams] = useSearchParams();

  /** 1️⃣ URL → condition */
  const condition = useMemo<DiarySearchCondition>(() => {
    return parseDiarySearchParams(searchParams);
  }, [searchParams]);

  /** 2️⃣ debounce는 keyword 기준 */
  const debouncedKeyword = useDebounce(condition.keyword, 300);

  const debouncedCondition = useMemo<DiarySearchCondition>(() => {
    return {
      ...condition,
      keyword: debouncedKeyword,
    };
  }, [condition, debouncedKeyword]);

  /** 3️⃣ setter들 */
  const setKeyword = (keyword: string) => {
    const next = {
      ...condition,
      keyword,
      page: 1, // 🔥 검색어 변경 시 페이지 리셋
    };

    setSearchParams(buildDiarySearchParams(next));
  };

  const setPage = (page: number) => {
    const next = {
      ...condition,
      page,
    };

    setSearchParams(buildDiarySearchParams(next));
  };

  return {
    condition,
    debouncedCondition,
    setKeyword,
    setPage,
  };
}


// user/hooks/useTodayHomeData.ts
import { useQuery } from "@tanstack/react-query";
import { fetchTodayDiary } from "@/features/diary/api/diaryApi";
import { type DiaryEntry } from "@/shared/types/diary";

export function useTodayHomeData() {
  const {
    data,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["diary", "today", "home"],
    queryFn: fetchTodayDiary,
    staleTime: 1000 * 60, // 1분
  });

  const entries: DiaryEntry[] = data?.entries ?? [];

  const averageEmotion =
    entries.length > 0
      ? entries.reduce((sum, e) => sum + e.emotion, 0) / entries.length
      : undefined;
    

  return {
    date: data?.date,
    entries,
    summary: data?.summary,
    averageEmotion,
    hasTodayDiary: entries.length > 0,

    // UI 제어용 상태
    isLoading,
    isError,
    error,
  };
}

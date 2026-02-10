import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { DailyDiaryData, DiaryListResponse } from "@/shared/types/diary";

export const fetchDiaryList = async (page: number, size = 10) => {
  const response = await http.get<DiaryListResponse>(
    `${API.DIARY}/diaries?page=${page-1}&size=${size}`
  );

  return response.data;
};

export const deleteDiaryEntry = async (entryId: number, withSummary = false) => {
  await http.delete(`${API.DIARY}/${entryId}?withSummary=${withSummary}`);
};

export const deleteGptSummaryOnly = async (date: string) => {
  await http.delete(`${API.DIARY}/summary?date=${date}`);
};

export const deleteDiarySummaryByDate = async (date: string) => {
  await http.delete(`${API.DIARY}/all?date=${date}`);
};

/** 
 * SummaryController 
 * Base /api/diary/summary
 * - EndPoints /today/home
 */
 export async function fetchTodayDiary(): Promise<DailyDiaryData> {
  return http.get(`${API.DIARY}/summary/today/home`).then(res => res.data);
}
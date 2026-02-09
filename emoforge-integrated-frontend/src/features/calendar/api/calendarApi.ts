import {http} from "@/shared/api/httpClient";
import { type DailyDiaryData } from "@/shared/types/diary";




//  월별 다이어리 목록을 가져오는 API
//selectedDate : YYYY-MM-DD 형식의 날짜 문자열
export const fetchMonthDiaryList = async (selectedDate:string) => {
  const response = await http.get<DailyDiaryData[]>(
    `/api/diary/diaries/monthly?yearMonth=${selectedDate}`
  );

  return response.data;
};

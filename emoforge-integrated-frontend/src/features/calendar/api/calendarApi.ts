import {http} from "@/shared/api/httpClient";


export type EmotionLevel = 1 | 2 | 3 | 4 | 5;

export type DiaryEntry = {
  id: number;
  date: string;
  emotion: EmotionLevel;
  habitTags: string;
  feelingKo: string;
  feelingEn: string;
  content: string;
  feedback: string;
};

export type DailyDiaryData = {
  date: string;
  entries: DiaryEntry[];
  summary?: string;
};

//  월별 다이어리 목록을 가져오는 API
//selectedDate : YYYY-MM-DD 형식의 날짜 문자열
export const fetchMonthDiaryList = async (selectedDate:string) => {
  const response = await http.get<DailyDiaryData[]>(
    `/api/diary/diaries/monthly?yearMonth=${selectedDate}`
  );

  return response.data;
};

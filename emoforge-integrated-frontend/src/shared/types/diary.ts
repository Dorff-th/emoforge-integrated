export type EmotionLevel = 1 | 2 | 3 | 4 | 5;

export type DiaryEntry = {
  id: number;
  emotion: EmotionLevel;
  habitTags: string;
  feelingKo: string;
  feelingEn: string;
  content: string;
  feedback: string;
  createdAt: string;
};

export type DailyDiaryData = {
  date: string;
  entries: DiaryEntry[];
  summary?: string;
};

export interface DiaryItemType {
  id: number;
  diaryDate: string;
  summary: string;
  emotion: string;
  habitTags: string[];
  feelingKo: string;
  feelingEn: string;
  content: string;
  gptFeedback: string;
  feedback: string;
}

export interface DiaryListResponse {
  content: DiaryGroup[];
  //dtoList: DiaryItemType[];
  totalPages: number;
  page: number;
}




export interface DiaryGroup {
  date: string;
  summary: string | null;
  entries: DiaryEntry[];
}
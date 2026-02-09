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
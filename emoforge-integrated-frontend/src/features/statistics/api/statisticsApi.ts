import {http} from "@/shared/api/httpClient";

export interface EmotionStatisticsResponse {
  averageEmotion: number;
  emotionFrequency: Record<number, number>;
  weeklyTrend: {
    weekLabel: string;
    averageEmotion: number;
  }[];
  dayOfWeekAverage: Record<string, number>;
}

export const fetchEmotionStatistics = async (
  startDate: string,
  endDate: string
): Promise<EmotionStatisticsResponse> => {
  const res = await http.get('/api/diary/statistics/emotion', {
    params: { startDate, endDate },
  });
  return res.data;
};

import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

export const getMusicRecommendations = async (diaryEntryId: number) => {
  const { data } = await http.get(`${API.DIARY}/music/${diaryEntryId}/recommendations`);
  return data;
};

export const requestMusicRecommendations = async (diaryEntryId: number, artistPreferences: string[]) => {
  const { data } = await http.post(`${API.DIARY}/music/recommend`, {
    diaryEntryId,
    artistPreferences,
  });
  return data;
};

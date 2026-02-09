import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";


export const generateGptSummary = async (date: string): Promise<string> => {
  const response = await http.post(`${API.DIARY}/gpt-summary`, {
    date
  });
  return response.data.summary;
};

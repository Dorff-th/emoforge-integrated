import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

export async function fetchSuggest(query: string) {
  const q = (query || '').trim();
  if (q.length < 2) return []; // 너무 짧으면 안 보냄 (간단 방어)

  try {
    const res = await http.get(`${API.POST}/tags/suggest?query=${encodeURIComponent(q)}`);

    const data = await res.data;
    // data가 ["spring","spring-boot"] 형태라고 가정
    if (Array.isArray(data)) return data.slice(0, 20);
    return [];
  } catch {
    return [];
  }
}
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { Category } from '@/features/post/types/Category';

//Category 목록 가져오기
export async function fetchCategories(): Promise<Category[]> {
  const res = await http.get(`${API.POST}/categories`);
  return res.data;
}
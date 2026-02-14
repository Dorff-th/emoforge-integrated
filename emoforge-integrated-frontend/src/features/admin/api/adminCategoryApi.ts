import { http } from '@/shared/api/httpClient';
import { API } from '@/shared/api/endpoints';
import type { Category } from '@/features/admin/api/admin.type';

export async function fetchCategories(): Promise<Category[]> {
  const res = await http.get(`${API.ADMIN.POST}/categories`);
  return res.data;
}

export async function createCategory(name: string): Promise<void> {
  await http.post(`${API.ADMIN.POST}/categories`, { name });
}

export async function updateCategory(id: number, name: string): Promise<void> {
  await http.put(`${API.ADMIN.POST}/categories/${id}`, { name });
}

export async function deleteCategory(id: number): Promise<void> {
  // ⚠️ 백엔드에서 자동으로 Post는 기본 카테고리로 이동 처리
  await http.delete(`${API.ADMIN.POST}/categories/${id}`);
}

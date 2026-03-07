import { http } from '@/shared/api/httpClient';
import { API } from '@/shared/api/endpoints';
import type { PostDTO, PostDetailDTO } from '@/features/post/types/Post';
import type { PageResponse } from '@/features/post/types/Common';

export type AdminPostSearchType = 'ALL' | 'TITLE' | 'CONTENT';

// 관리자 화면 게시글 목록 조회
export async function fetchAdminPosts(
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
  searchType: AdminPostSearchType = 'ALL',
  keyword?: string,
): Promise<PageResponse<PostDTO>> {
  const res = await http.get(`${API.ADMIN.POST}/posts`, {
    params: {
      page,
      size,
      sort,
      direction,
      searchType,
      keyword: keyword?.trim() ? keyword.trim() : undefined,
    },
  });
  return res.data;
}

// 관리자 화면 게시글 상세 조회
export async function fetchAdminPostDetail(postId: number): Promise<PostDetailDTO> {
  const res = await http.get(`${API.ADMIN.POST}/posts/${postId}`);
  return res.data;
}

export async function bulkDeleteAdminPosts(postIds: number[]): Promise<void> {
  await http.delete(`${API.ADMIN.POST}/posts`, {
    data: { postIds },
  });
}

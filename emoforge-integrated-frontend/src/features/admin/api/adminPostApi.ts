import { http } from '@/shared/api/httpClient';
import { API } from '@/shared/api/endpoints';
import type { PostDTO, PostDetailDTO } from '@/features/post/types/Post';
import type { PageResponse } from '@/features/post/types/Common';
import type { CommentResponse } from '@/features/post/types/Comment';

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

// 관리자 화면 게시글 상세 조회 요청
export async function fetchAdminPostDetail(postId: number): Promise<PostDetailDTO> {
  const res = await http.get(`${API.ADMIN.POST}/posts/${postId}`);
  return res.data;
}

//관리자 화면 게시판 목록 일괄삭제 요청
export async function bulkDeleteAdminPosts(postIds: number[]): Promise<void> {
  await http.delete(`${API.ADMIN.POST}/posts`, {
    data: { postIds },
  });
}

//관리자 화면 게시판 상세에서 단일삭제 요청
export const deleteAdminPost = async (postId: number) => {
  const res = await http.delete(`${API.ADMIN.POST}/posts/${postId}`);
  return res.data;
};

//관리자 화면 게시판 상세에서 댓글 목록 요청
export async function fetchAdminCommentsByPostId(postId: number): Promise<CommentResponse[]> {
  const res = await http.get<CommentResponse[]>(`${API.ADMIN.POST}/posts/${postId}/comments`);
 
  return res.data;
}


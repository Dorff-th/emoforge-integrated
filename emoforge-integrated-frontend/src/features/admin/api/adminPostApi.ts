import { http } from '@/shared/api/httpClient';
import { API } from '@/shared/api/endpoints';
import type { PostDTO, PostDetailDTO } from '@/features/post/types/Post';
import type { PageResponse } from '@/features/post/types/Common';



//관리자 화면 게시판 목록 조회
export async function fetchAdminPosts(
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
): Promise<PageResponse<PostDTO>> {   
    const res = await http.get(`${API.ADMIN.POST}/posts`, {
        params: { page, size, sort, direction },
    });
    return res.data;
}

//관리자 화면 게시판 상세 조회
export async function fetchAdminPostDetail(postId: number): Promise<PostDetailDTO> {
  const res = await http.get(`${API.ADMIN.POST}/posts/${postId}`);
  return res.data;
}
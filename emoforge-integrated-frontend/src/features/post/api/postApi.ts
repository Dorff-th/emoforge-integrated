import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import type { PostDTO, PostDetailDTO } from '@/features/post/types/Post';
import type { PageResponse } from '@/features/post/types/Common';
import type { CommentResponse } from '@/features/post/types/Comment';
import type { Tag } from "@/features/post/types/Tag";

export const fetchPosts = async (
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
): Promise<PageResponse<PostDTO>> => {
  const response = await http.get<PageResponse<PostDTO>>(`${API.POST}`, {
    params: { page, size, sort, direction },
  });
  return response.data;
};

// 게시글 상세 조회
export const fetchPostDetail = async (id: number): Promise<PostDetailDTO> => {
  const response = await http.get<PostDetailDTO>(`${API.POST}/${id}`);
  return response.data;
};

// 📌 게시글에 달린 태그 목록 조회
export async function getPostTags(postId: number): Promise<Tag[]> {
  const res = await http.get(`${API.POST}/${postId}/tags`);
  return res.data;
}

// 게시글에 달린 댓글 목록 조회
export async function fetchCommentsByPostId(postId: number): Promise<CommentResponse[]> {
  const res = await http.get<CommentResponse[]>(`${API.POST}/${postId}/comments`);
 
  return res.data;
}

// 댓글 작성
export async function createComment(postId: number, content: string): Promise<CommentResponse> {
  const res = await http.post<CommentResponse>(`${API.POST}/${postId}/comments`, { content });
  return res.data;
}

// 댓글 삭제
export async function deleteComment(postId: number, commentId: number): Promise<void> {
  await http.delete(`${API.POST}/${postId}/comments/${commentId}`);
}

// 태그로 게시글 조회
export const fetchPostsByTag = async (
  tagName: string,
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
): Promise<PageResponse<PostDTO>> => {
  const response = await http.get<PageResponse<PostDTO>>(`${API.POST}/tags/${tagName}`, {
    params: { page, size, sort, direction },
  });
  return response.data;
};

//게시글 삭제
export const deletePost = async (postId: number) => {
  const res = await http.delete(`${API.POST}/${postId}`);
  return res.data;
};


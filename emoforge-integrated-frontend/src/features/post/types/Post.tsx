import type { Attachment } from "./Attachment";
// types/post.ts
// 게시글 목록용 DTO
export interface PostDTO {
  postId: number;
  categoryName: string;
  title: string;

  memberUuid: string;
  nickname: string; // ✅ username 제외

  createdAt: string;

  commentCount: number;
  attachmentCount: number;
}

// 게시글 상세보기용 DTO
export interface PostDetailDTO {
  id: number;
  title: string;
  content: string;

  createdAt: string;
  updatedAt: string;

  categoryName: string;
  categoryId: number;

  memberUuid: string;
  username: string;
  nickname: string;

  attachments: Attachment[]; // ✅ 추가

  adminModifiedAt: string;
  adminModifiedByNickname: string;

  commentCount: number;
  attachmentCount: number;
}

export interface Tag {
  id: number;
  name: string;
}

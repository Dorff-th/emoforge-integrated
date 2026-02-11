export interface CommentResponse {
  id: number;
  content: string;
  createdAt: string; // ISO 문자열로 내려오기 때문에 string
  postId: number;
  memberUuid: string;
  username: string;
  nickname: string;
  profileImageUrl: string | null;
}

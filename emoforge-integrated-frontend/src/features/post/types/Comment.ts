//사용자 페이지의 댓글응답 타입
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

//관리자 페이지의 댓글응답 타입
export interface AdminCommentResponse {
  commentId: number;
  content: string;
  createdAt: string; // ISO 문자열로 내려오기 때문에 string
  postId: number;
  memberUuid: string;
  author: string;   // 작성자 ()
  postTitle: string;  // 게시글 제목  
  
}



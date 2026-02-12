import { useEffect, useState } from "react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import {
  fetchCommentsByPostId,
  createComment,
  deleteComment,
} from "@/features/post/api/postApi";
import type { CommentResponse } from "@/features/post/types/Comment";
import { useProfileImage } from "@/features/user/hooks/useProfileImage";
import { Avatar } from "@/shared/components/Avatar";
import { useToast } from "@/shared/stores/useToast";
import ConfirmModal from "@/shared/components/ConfirmModal";
import { MessageSquare, Send, Trash2 } from "lucide-react";

interface UserCommentProps {
  postId: number;
}

export default function UserComment({ postId }: UserCommentProps) {
  const [comments, setComments] = useState<CommentResponse[]>([]);
  const [newComment, setNewComment] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<CommentResponse | null>(
    null,
  );
  //const currentUser = useSelector((state: RootState) => state.auth.user);

  const { user, isAuthenticated } = useAuth();
  const currentUser = user;
  const toast = useToast();
  const { publicUrl } = useProfileImage(user?.uuid);

  const [open, setOpen] = useState(false);

  // 댓글 목록 불러오기
  const loadComments = async () => {
    const data = await fetchCommentsByPostId(postId);
    setComments(data);
  };

  useEffect(() => {
    loadComments();
  }, [postId]);

  // 댓글 작성
  const handleSubmit = async () => {
    if (!newComment.trim()) return;

    try {
      await createComment(postId, newComment);
      toast.success("댓글이 등록되었습니다.");
    } catch (error) {
      toast.error("댓글 등록 실패");
    } finally {
      setNewComment("");
      await loadComments();
    }
  };

  // 댓글 삭제
  const handleDelete = async (commentId: number) => {
    try {
      await deleteComment(postId, commentId);
      toast.success("댓글이 삭제되었습니다.");
    } catch (error) {
      toast.error("댓글 삭제 실패");
    } finally {
      await loadComments();
    }
  };

  return (
    <div className="mt-6">
      <div className="comment-header flex items-center gap-2 mb-4">
        <MessageSquare size={18} className="text-gray-500" />
        <span className="text-sm font-medium text-gray-600">Comments</span>
      </div>

      {/* 입력창 */}
      {isAuthenticated && (
        <div className="flex gap-3 mb-4">
          <Avatar
            publicUrl={publicUrl}
            name={currentUser?.nickname}
            size={40}
          />
          <textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="댓글을 입력하세요..."
            className="flex-1 border rounded p-2 resize-none"
          />

          <button className="comment-post-btn" onClick={handleSubmit}>
            <Send size={18} />
            <span className="comment-post-label">Post</span>
          </button>
        </div>
      )}

      {/* 댓글 목록 */}
      <div className="space-y-3">
        {comments.length > 0 ? (
          comments.map((c) => (
            <div
              key={c.id}
              className="group relative p-4 border rounded-md bg-white hover:shadow-md transition flex gap-3"
            >
              <Avatar
                publicUrl={c.profileImageUrl}
                name={c.nickname}
                size={40}
              />

              <div className="flex-1">
                <div className="flex items-center justify-between mb-1">
                  <span className="font-medium text-gray-700">
                    {c.nickname}
                  </span>
                  <span className="text-sm text-gray-500">
                    {new Date(c.createdAt).toLocaleString()}
                  </span>
                </div>
                <p className="text-gray-800 whitespace-pre-line">{c.content}</p>
              </div>

              {/* 본인 댓글일 때만 삭제 버튼 */}
              {currentUser?.uuid === c.memberUuid && (
                <button
                  onClick={() => {
                    setDeleteTarget(c);
                    setOpen(true); // ✅ 모달 열기
                  }}
                  className="comment-delete"
                >
                  <Trash2 size={14} />
                </button>
              )}
            </div>
          ))
        ) : (
          <div className="p-4 text-center text-gray-500 border rounded-md bg-gray-50">
            No Comments.
          </div>
        )}
      </div>

      {/* 삭제 확인 모달 */}
      {deleteTarget && (
        <ConfirmModal
          open={open}
          title="댓글 삭제"
          description={`정말 이 댓글을 삭제하시겠습니까?`}
          onConfirm={() => handleDelete(deleteTarget.id)} // ✅ 인자 없는 함수
          onCancel={() => setOpen(false)}
        />
      )}
    </div>
  );
}

import { useEffect, useState } from "react";
import {
  createComment,
  deleteComment,
  fetchCommentsByPostId,
} from "@/features/post/api/postApi";
import { useAuth } from "@/features/auth/hooks/useAuth";
import type { CommentResponse } from "@/features/post/types/Comment";
import { useProfileImage } from "@/features/user/hooks/useProfileImage";
import { Avatar } from "@/shared/components/Avatar";
import ConfirmModal from "@/shared/components/ConfirmModal";
import { useToast } from "@/shared/stores/useToast";
import { MessageSquare, Send, Trash2 } from "lucide-react";

interface UserCommentProps {
  postId: number;
}

const formatCommentDate = (isoDate: string) =>
  new Date(isoDate).toLocaleString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });

export default function UserComment({ postId }: UserCommentProps) {
  const [comments, setComments] = useState<CommentResponse[]>([]);
  const [newComment, setNewComment] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<CommentResponse | null>(
    null,
  );
  const [open, setOpen] = useState(false);

  const { user, isAuthenticated } = useAuth();
  const { publicUrl } = useProfileImage(user?.uuid);
  const toast = useToast();

  const loadComments = async () => {
    const data = await fetchCommentsByPostId(postId);
    setComments(data);
  };

  useEffect(() => {
    loadComments();
  }, [postId]);

  const handleSubmit = async () => {
    if (!newComment.trim()) return;

    try {
      await createComment(postId, newComment);
      toast.success("댓글을 등록했습니다.");
    } catch {
      toast.error("댓글 등록에 실패했습니다.");
    } finally {
      setNewComment("");
      await loadComments();
    }
  };

  const handleDelete = async (commentId: number) => {
    try {
      await deleteComment(postId, commentId);
      toast.success("댓글을 삭제했습니다.");
    } catch {
      toast.error("댓글 삭제에 실패했습니다.");
    } finally {
      setOpen(false);
      setDeleteTarget(null);
      await loadComments();
    }
  };

  return (
    <section>
      <div className="mb-5 flex items-center justify-between gap-3">
        <div className="flex items-center gap-2">
          <MessageSquare size={18} className="text-stone-500" />
          <h2 className="text-lg font-semibold text-stone-900">Comments</h2>
        </div>
        <span className="text-sm text-stone-500">{comments.length} items</span>
      </div>

      {isAuthenticated && (
        <div className="mb-6 rounded-2xl border border-stone-200 bg-white p-4 shadow-sm">
          <div className="flex items-start gap-3">
            <Avatar publicUrl={publicUrl} name={user?.nickname} size={44} />
            <div className="flex-1">
              <div className="mb-2 text-sm font-medium text-stone-700">
                {user?.nickname}
              </div>
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                placeholder="댓글을 입력해주세요."
                className="min-h-28 w-full resize-none rounded-2xl border border-stone-200 bg-stone-50 px-4 py-3 text-sm text-stone-800 outline-none transition placeholder:text-stone-400 focus:border-stone-300 focus:bg-white"
              />
            </div>
          </div>

          <div className="mt-3 flex justify-end">
            <button
              type="button"
              className="inline-flex items-center gap-2 rounded-full bg-stone-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-stone-700"
              onClick={handleSubmit}
            >
              <Send size={16} />
              Post Comment
            </button>
          </div>
        </div>
      )}

      <div className="space-y-4">
        {comments.length > 0 ? (
          comments.map((comment) => (
            <article
              key={comment.id}
              className="rounded-2xl border border-stone-200 bg-white p-5 shadow-sm"
            >
              <div className="flex items-start gap-3">
                <Avatar
                  publicUrl={comment.profileImageUrl}
                  name={comment.nickname}
                  size={44}
                />

                <div className="min-w-0 flex-1">
                  <div className="flex flex-col gap-1 sm:flex-row sm:items-start sm:justify-between">
                    <div>
                      <div className="font-medium text-stone-900">
                        {comment.nickname}
                      </div>
                      <div className="text-sm text-stone-500">
                        {formatCommentDate(comment.createdAt)}
                      </div>
                    </div>

                    {user?.uuid === comment.memberUuid && (
                      <button
                        type="button"
                        onClick={() => {
                          setDeleteTarget(comment);
                          setOpen(true);
                        }}
                        className="inline-flex items-center gap-1 self-start rounded-full px-3 py-1 text-sm text-stone-400 transition hover:bg-rose-50 hover:text-rose-600"
                      >
                        <Trash2 size={14} />
                        Delete
                      </button>
                    )}
                  </div>

                  <p className="mt-3 whitespace-pre-line break-words text-[15px] leading-7 text-stone-700">
                    {comment.content}
                  </p>
                </div>
              </div>
            </article>
          ))
        ) : (
          <div className="rounded-lg border border-dashed border-gray-300 py-4 text-center text-sm text-gray-500">
            아직 댓글이 없습니다.
          </div>
        )}
      </div>

      {deleteTarget && (
        <ConfirmModal
          open={open}
          title="댓글 삭제"
          description="정말로 이 댓글을 삭제하시겠습니까?"
          onConfirm={() => handleDelete(deleteTarget.id)}
          onCancel={() => {
            setOpen(false);
            setDeleteTarget(null);
          }}
        />
      )}
    </section>
  );
}

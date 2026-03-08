import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  fetchAdminPostDetail,
  deleteAdminPost,
} from "@/features/admin/api/adminPostApi";
import { getPostTags } from "@/features/post/api/postApi";
import type { PostDetailDTO, Tag } from "@/features/post/types/Post";
import { useToast } from "@/shared/stores/useToast";
import { Viewer } from "@toast-ui/react-editor";
import { backendBaseUrl, serverBaseUrl } from "@/shared/config/config";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useUILoading } from "@/shared/stores/useUILoading";
import { PaperclipIcon, ArrowLeft, Pencil, Trash2 } from "lucide-react";
import ConfirmModal from "@/shared/components/ConfirmModal";

export default function AdminPostPage() {
  const { id } = useParams<{ id: string }>();
  const [post, setPost] = useState<PostDetailDTO | null>(null);
  const [open, setOpen] = useState(false);
  const [_openConfirm, _setOpenConfirm] = useState(false);

  const toast = useToast();
  const navigate = useNavigate();

  const postId: number = Number(id);

  const [tags, setTags] = useState<Tag[]>([]);

  useEffect(() => {
    if (id) {
      fetchAdminPostDetail(Number(id)).then(setPost).catch(console.error);
    }
  }, [id]);

  useEffect(() => {
    getPostTags(postId)
      .then(setTags)
      .catch(() => setTags([]));
  }, [postId]);

  const handleDelete = async (postIdToDelete: number) => {
    try {
      const result = await deleteAdminPost(postIdToDelete);
      toast.success("게시글이 삭제되었습니다.");
      navigate("/admin/posts");
      if (result !== null) {
        navigate("/admin/posts");
        return true;
      }
    } catch {
      toast.error("게시글 삭제 실패");
    }

    return false;
  };

  const handleConfirm = async (postIdToDelete: number) => {
    setOpen(false);
    await handleDelete(postIdToDelete);
  };

  const content = post?.content.replace(
    /\/uploads\//g,
    `${serverBaseUrl}/uploads/`,
  );

  const formatAdminModifiedAt = (isoDate?: string) => {
    if (!isoDate) return "";
    const date = new Date(isoDate);
    if (Number.isNaN(date.getTime())) return isoDate;

    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    const hh = String(date.getHours()).padStart(2, "0");
    const min = String(date.getMinutes()).padStart(2, "0");

    return `${yyyy}-${mm}-${dd} ${hh}:${min}`;
  };

  useUILoading("admin:post:detail", { duration: 150 });

  return (
    <SectionLoading scope="admin:post:detail">
      <div className="mx-auto max-w-6xl px-5 bg-gray-100 rounded-xl p-6">
        <h2 className="post-title mb-2">{post?.title}</h2>

        {/* 작성자 + 작성일 */}
        <div className="post-meta mb-2">
          {post?.categoryName} · {post?.nickname} ·{" "}
          {post?.createdAt
            ? new Date(post.createdAt).toLocaleString("ko-KR")
            : "-"}{" "}
          {/* createdAt이 있을 때만 날짜 변환/출력  */}
        </div>

        {/* 본문 */}
        <div className="border rounded-lg p-4 bg-white shadow mb-6">
          {content != null && <Viewer key={post?.id} initialValue={content} />}

          {/* {content} */}
          {post?.adminModifiedAt && (
            <div className="text-xs text-[#888] italic text-right mt-3">
              관리자 수정 · {formatAdminModifiedAt(post.adminModifiedAt)} (
              {post.adminModifiedByNickname})
            </div>
          )}
        </div>

        {/* ✅ 태그 리스트 */}
        {tags?.length ? (
          <div className="flex flex-wrap gap-2 mb-4">
            {tags.map((tag) => (
              <span key={tag.id} className="tag">
                #{tag.name}
              </span>
            ))}
          </div>
        ) : null}
        {/* ✅ 첨부파일 리스트 */}
        {post?.attachments && post.attachments.length > 0 && (
          <div className="attachment-section">
            <div className="attachment-title">
              <PaperclipIcon className="attachment-icon" />
              <span>Attachment</span>
            </div>
            <ul className="attachment-list">
              {post.attachments.map((att) => (
                <li key={att.id}>
                  <a
                    href={`${backendBaseUrl}/download/${att.id}`}
                    className="attachment-link"
                  >
                    {att.originFileName}
                    <span className="attachment-size">
                      ({att.fileSizeText})
                    </span>
                  </a>
                </li>
              ))}
            </ul>
          </div>
        )}
        {/* 버튼 영역 */}
        <div className="flex space-x-2 mt-7">
          <button
            onClick={() => navigate("/admin/posts")}
            className="icon-btn"
            title="목록으로"
          >
            <ArrowLeft size={16} />
            <span className="icon-label">목록으로</span>
          </button>
          <button
            onClick={() => navigate(`/admin/posts/${post?.id}/edit`)}
            className="icon-btn"
            title="Edit"
          >
            <Pencil size={16} />
            <span className="icon-label">수정</span>
          </button>
          <button
            onClick={() => setOpen(true)}
            className="icon-btn danger"
            title="Delete"
          >
            <Trash2 size={16} />
            <span className="icon-label">삭제</span>
          </button>
        </div>
        <ConfirmModal
          open={open}
          title="게시물 삭제"
          description={`정말로 "${post?.title}" 게시물을 삭제하시겠습니까?`}
          onConfirm={() => {
            if (post?.id == null) return;
            handleConfirm(post.id);
          }}
          onCancel={() => setOpen(false)}
        />
        <hr className="my-8 border-gray-200" />
      </div>
    </SectionLoading>
  );
}

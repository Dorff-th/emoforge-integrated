import { useEffect, useState, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import {
  fetchPostDetail,
  getPostTags,
  deletePost,
} from "@/features/post/api/postApi";
import type { PostDetailDTO, Tag } from "../types/Post";
import { Viewer } from "@toast-ui/react-editor";
import ConfirmModal from "@/shared/components/ConfirmModal";
import { useToast } from "@/shared/stores/useToast";
import UserComment from "@/features/post/components/UserComment";
import { backendBaseUrl } from "@/shared/config/config";
import { PaperclipIcon, ArrowLeft, Pencil, Trash2 } from "lucide-react";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useUILoading } from "@/shared/stores/useUILoading";

const PostDetailPage = () => {
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
      fetchPostDetail(Number(id)).then(setPost).catch(console.error);
    }
  }, [id]);

  useEffect(() => {
    getPostTags(postId)
      .then(setTags)
      .catch(() => setTags([]));
  }, [postId]);

  const { user, isAuthenticated } = useAuth();
  const currentUser = user;

  const isAuthor = useMemo(() => {
    if (!isAuthenticated || !post?.memberUuid || !currentUser?.uuid) {
      return null; // 아직 준비 전 상태
    }

    return currentUser.uuid === post.memberUuid; // 준비 완료 → true/false 반환
  }, [isAuthenticated, currentUser?.uuid, post?.memberUuid]);

  const handleDelete = async (postIdToDelete: number) => {
    try {
      const result = await deletePost(postIdToDelete);
      toast.success("게시글이 삭제되었습니다.");
      navigate("/");
      if (result !== null) {
        navigate("/");
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
    `${backendBaseUrl}/uploads/`,
  );

  useUILoading("user:post:detail", { duration: 150 });

  return (
    <SectionLoading scope="user:post:detail">
      <div className="mx-auto max-w-6xl px-5 bg-gray-100 rounded-xl p-6">
        {/* 제목 */}
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
          <Viewer
            initialValue={content}
            customHTMLRenderer={{
              image(node: any, { entering }: { entering: boolean }) {
                if (!entering) return;

                const alt =
                  node.altText && node.altText.trim().length > 0
                    ? node.altText
                    : "";

                return {
                  type: "openTag",
                  tagName: "img",
                  attributes: {
                    src: node.destination,
                    alt: alt,
                    onerror: "this.style.display='none'",
                  },
                };
              },
            }}
          />
        </div>

        {/* ✅ 태그 리스트 */}
        {tags?.length ? (
          <div className="flex flex-wrap gap-2 mb-4">
            {tags.map((tag) => (
              <span
                key={tag.id}
                className="tag cursor-pointer"
                onClick={() => navigate(`/tags/${tag.name}`)}
              >
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
            onClick={() => navigate("/")}
            className="icon-btn"
            title="To List"
          >
            <ArrowLeft size={16} />
            <span className="icon-label">List</span>
          </button>

          {isAuthor && (
            <div className="flex gap-2">
              <button
                onClick={() => navigate(`/${post?.id}/edit`)}
                className="icon-btn"
                title="Edit"
              >
                <Pencil size={16} />
                <span className="icon-label">Modify</span>
              </button>
              <button
                onClick={() => setOpen(true)}
                className="icon-btn danger"
                title="Delete"
              >
                <Trash2 size={16} />
                <span className="icon-label">Delete</span>
              </button>
            </div>
          )}
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
        </div>

        <hr className="my-8 border-gray-200" />

        {/* 댓글 관리 섹션 */}
        <UserComment postId={postId} />
      </div>
    </SectionLoading>
  );
};

export default PostDetailPage;

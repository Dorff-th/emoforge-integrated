import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Viewer } from "@toast-ui/react-editor";
import {
  ArrowLeft,
  CalendarDays,
  ChevronRight,
  Files,
  PaperclipIcon,
  Pencil,
  TagIcon,
  Trash2,
  UserRound,
} from "lucide-react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import {
  deletePost,
  fetchPostDetail,
  fetchPosts,
  getPostTags,
} from "@/features/post/api/postApi";
import type { PostDTO, PostDetailDTO, Tag } from "../types/Post";
import ConfirmModal from "@/shared/components/ConfirmModal";
import { backendBaseUrl, serverBaseUrl } from "@/shared/config/config";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useToast } from "@/shared/stores/useToast";
import { useUILoading } from "@/shared/stores/useUILoading";
import UserComment from "@/features/post/components/UserComment";
import PostNavigation from "@/features/post/components/PostNavigation/PostNavigation";

interface AdjacentPosts {
  previous: PostDTO | null;
  next: PostDTO | null;
}

const formatDateTime = (isoDate?: string) => {
  if (!isoDate) return "-";

  const date = new Date(isoDate);
  if (Number.isNaN(date.getTime())) return isoDate;

  return date.toLocaleString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

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

const PostDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const toast = useToast();
  const { user, isAuthenticated } = useAuth();

  const [post, setPost] = useState<PostDetailDTO | null>(null);
  const [tags, setTags] = useState<Tag[]>([]);
  const [adjacentPosts, setAdjacentPosts] = useState<AdjacentPosts>({
    previous: null,
    next: null,
  });
  const [open, setOpen] = useState(false);

  const postId = Number(id);
  const currentUser = user;

  useEffect(() => {
    if (!id) return;

    fetchPostDetail(Number(id)).then(setPost).catch(console.error);
  }, [id]);

  useEffect(() => {
    if (!postId) return;

    getPostTags(postId)
      .then(setTags)
      .catch(() => setTags([]));
  }, [postId]);

  useEffect(() => {
    if (!post?.id) return;

    fetchPosts(1, 1000, "createdAt", "DESC", post.categoryId)
      .then((data) => {
        const currentIndex = data.dtoList.findIndex(
          (item) => item.postId === post.id,
        );

        if (currentIndex === -1) {
          setAdjacentPosts({ previous: null, next: null });
          return;
        }

        setAdjacentPosts({
          previous: data.dtoList[currentIndex - 1] ?? null,
          next: data.dtoList[currentIndex + 1] ?? null,
        });
      })
      .catch(() =>
        setAdjacentPosts({
          previous: null,
          next: null,
        }),
      );
  }, [post?.id, post?.categoryId]);

  const isAuthor = useMemo(() => {
    if (!isAuthenticated || !post?.memberUuid || !currentUser?.uuid) {
      return null;
    }

    return currentUser.uuid === post.memberUuid;
  }, [currentUser?.uuid, isAuthenticated, post?.memberUuid]);

  const handleDelete = async (postIdToDelete: number) => {
    try {
      const result = await deletePost(postIdToDelete);
      toast.success("게시글을 삭제했습니다.");
      navigate("/posts");

      if (result !== null) {
        navigate("/posts");
        return true;
      }
    } catch {
      toast.error("게시글 삭제에 실패했습니다.");
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

  useUILoading("user:post:detail", { duration: 150 });

  return (
    <SectionLoading scope="user:post:detail">
      <div className="mx-auto max-w-5xl px-4 py-6 sm:px-6 bg-gray-100 rounded-2xl">
        <div className="rounded-[28px] border border-gray-300 bg-gradient-to-b from-stone-50 via-white to-stone-50 p-6 shadow-[0_16px_50px_rgba(28,25,23,0.08)] sm:p-10">
          <nav
            aria-label="Breadcrumb"
            className="mb-6 flex flex-wrap items-center gap-2 text-sm text-stone-500"
          >
            <button
              type="button"
              className="transition hover:text-stone-900"
              onClick={() => navigate("/posts")}
            >
              Posts
            </button>
            <ChevronRight size={14} />
            <span className="text-stone-500">
              {post?.categoryName ?? "Category"}
            </span>
            <ChevronRight size={14} />
            <span className="max-w-full truncate font-medium text-stone-800">
              {post?.title ?? "Post"}
            </span>
          </nav>

          <header className="border-b border-gray-300/70 pb-8">
            <p className="mb-3 inline-flex items-center rounded-full bg-stone-200/70 px-3 py-1 text-xs font-semibold uppercase tracking-[0.24em] text-stone-600">
              {post?.categoryName ?? "Post"}
            </p>

            <h1 className="max-w-3xl text-3xl font-bold leading-tight text-stone-950 sm:text-4xl">
              {post?.title}
            </h1>

            <div className="mt-5 flex flex-wrap items-center gap-x-5 gap-y-2 text-sm text-stone-500">
              <span className="inline-flex items-center gap-2">
                <UserRound size={16} />
                {post?.nickname ?? "-"}
              </span>
              <span className="inline-flex items-center gap-2">
                <CalendarDays size={16} />
                {formatDateTime(post?.createdAt)}
              </span>
              {post?.updatedAt && post.updatedAt !== post.createdAt ? (
                <span>Updated {formatDateTime(post.updatedAt)}</span>
              ) : null}
            </div>
          </header>

          <article className="mx-auto max-w-3xl py-8">
            <div className="rounded-xl border border-gray-300 bg-white p-8 shadow-sm">
              <div className="max-w-none text-[17px] leading-8 text-stone-800 [&_.toastui-editor-contents]:font-inherit [&_.toastui-editor-contents]:text-inherit [&_.toastui-editor-contents]:leading-8 [&_.toastui-editor-contents_h1]:mt-10 [&_.toastui-editor-contents_h1]:text-3xl [&_.toastui-editor-contents_h1]:font-bold [&_.toastui-editor-contents_h2]:mt-8 [&_.toastui-editor-contents_h2]:text-2xl [&_.toastui-editor-contents_h2]:font-semibold [&_.toastui-editor-contents_h3]:mt-6 [&_.toastui-editor-contents_h3]:text-xl [&_.toastui-editor-contents_p]:my-5 [&_.toastui-editor-contents_pre]:overflow-x-auto [&_.toastui-editor-contents_pre]:rounded-2xl [&_.toastui-editor-contents_pre]:bg-stone-900 [&_.toastui-editor-contents_pre]:p-4 [&_.toastui-editor-contents_blockquote]:border-l-4 [&_.toastui-editor-contents_blockquote]:border-stone-300 [&_.toastui-editor-contents_blockquote]:pl-4 [&_.toastui-editor-contents_img]:mx-auto [&_.toastui-editor-contents_img]:rounded-2xl">
                <Viewer
                  key={post?.id ?? "empty"}
                  initialValue={content ?? ""}
                />
              </div>

              {post?.adminModifiedAt && (
                <p className="mt-6 text-right text-xs italic text-stone-500">
                  관리자 수정 · {formatAdminModifiedAt(post.adminModifiedAt)} (
                  {post.adminModifiedByNickname})
                </p>
              )}
            </div>
          </article>

          {tags.length > 0 && (
            <section className="mx-auto mb-8 flex max-w-3xl flex-wrap items-center gap-2">
              <span className="mr-1 inline-flex items-center gap-2 text-sm font-medium text-stone-500">
                <TagIcon size={15} />
                Tags
              </span>
              {tags.map((tag) => (
                <button
                  key={tag.id}
                  type="button"
                  className="rounded-full bg-stone-100 px-3 py-1 text-sm font-medium text-stone-700 transition hover:bg-stone-200"
                  onClick={() => navigate(`/posts/tags/${tag.name}`)}
                >
                  #{tag.name}
                </button>
              ))}
            </section>
          )}

          {post?.attachments && post.attachments.length > 0 && (
            <section className="mx-auto mb-8 max-w-3xl rounded-xl border border-gray-300 bg-gray-50 p-5">
              <div className="mb-4 flex items-center gap-2 text-sm font-semibold text-stone-700">
                <Files size={16} />
                <span>Attachments</span>
              </div>
              <ul className="space-y-3">
                {post.attachments.map((attachment) => (
                  <li key={attachment.id}>
                    <a
                      href={`${backendBaseUrl}/download/${attachment.id}`}
                      className="flex items-center justify-between gap-3 rounded-xl border border-gray-300 px-4 py-3 text-sm transition hover:border-gray-400 hover:bg-white"
                    >
                      <span className="flex min-w-0 items-center gap-3 text-stone-700">
                        <PaperclipIcon
                          size={16}
                          className="shrink-0 text-stone-400"
                        />
                        <span className="truncate">
                          {attachment.originFileName}
                        </span>
                      </span>
                      <span className="shrink-0 text-stone-400">
                        {attachment.fileSizeText}
                      </span>
                    </a>
                  </li>
                ))}
              </ul>
            </section>
          )}

          <div className="mx-auto flex max-w-3xl flex-col gap-3 border-t border-gray-300/70 pt-8 sm:flex-row sm:items-center sm:justify-between">
            <button
              type="button"
              onClick={() => navigate("/posts")}
              className="inline-flex items-center justify-center gap-2 rounded-full border border-stone-300 px-4 py-2 text-sm font-medium text-stone-700 transition hover:border-stone-400 hover:bg-stone-100"
            >
              <ArrowLeft size={16} />
              List
            </button>

            {isAuthor && (
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => navigate(`/user/posts/${post?.id}/edit`)}
                  className="inline-flex items-center justify-center gap-2 rounded-full border border-stone-300 px-4 py-2 text-sm font-medium text-stone-700 transition hover:border-stone-400 hover:bg-stone-100"
                >
                  <Pencil size={16} />
                  Edit
                </button>
                <button
                  type="button"
                  onClick={() => setOpen(true)}
                  className="inline-flex items-center justify-center gap-2 rounded-full border border-rose-200 bg-rose-50 px-4 py-2 text-sm font-medium text-rose-700 transition hover:border-rose-300 hover:bg-rose-100"
                >
                  <Trash2 size={16} />
                  Delete
                </button>
              </div>
            )}
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

          <PostNavigation
            previousPost={
              adjacentPosts.previous
                ? {
                    id: adjacentPosts.previous.postId,
                    title: adjacentPosts.previous.title,
                  }
                : null
            }
            nextPost={
              adjacentPosts.next
                ? {
                    id: adjacentPosts.next.postId,
                    title: adjacentPosts.next.title,
                  }
                : null
            }
            categoryName={post?.categoryName ?? "Posts"}
          />

          <div className="mx-auto mt-10 max-w-3xl border-t border-gray-300/70 pt-8">
            <UserComment postId={postId} />
          </div>
        </div>
      </div>
    </SectionLoading>
  );
};

export default PostDetailPage;

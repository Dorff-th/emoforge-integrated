import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { fetchPosts } from "@/features/post/api/postApi";
import { fetchCategories } from "@/features/post/api/categoryApi";
import Pagination from "@/features/post/components/Pagination";
import CategoryFilter from "@/features/post/components/CategoryFilter";
import NewPostButton from "@/shared/components/NewPostButton";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useToast } from "@/shared/stores/useToast";
import { useUILoading } from "@/shared/stores/useUILoading";
import { LayoutList, MessageCircle, Paperclip } from "lucide-react";
import type { PageResponse } from "../types/Common";
import type { Category } from "../types/Category";
import type { PostDTO } from "../types/Post";

export default function PostListPage() {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(
    null,
  );

  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const toast = useToast();

  const loadPosts = async (pageNum: number) => {
    try {
      const data = await fetchPosts(
        pageNum,
        10,
        "createdAt",
        "DESC",
        selectedCategoryId ?? undefined,
      );

      setPosts(data.dtoList);
      setPageInfo(data);
      setPage(pageNum);
    } catch {
      toast.error("게시글 로드 실패");
    }
  };

  useEffect(() => {
    const loadCategories = async () => {
      try {
        const data = await fetchCategories();
        setCategories(data);
      } catch {
        toast.error("카테고리 로드 실패");
      }
    };

    loadCategories();
  }, []);

  useEffect(() => {
    loadPosts(1);
  }, [selectedCategoryId]);

  useUILoading("user:posts:list", { duration: 150 });

  return (
    <SectionLoading scope="user:posts:list">
      <div className="mx-auto max-w-6xl rounded-xl bg-gray-100 p-6 px-5">
        <div className="mb-4 flex items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <LayoutList size={20} />
            <h1 className="text-lg font-semibold">Posts</h1>
          </div>

          {isAuthenticated && <NewPostButton />}
        </div>

        <CategoryFilter
          categories={categories}
          selectedCategoryId={selectedCategoryId}
          onSelect={setSelectedCategoryId}
        />

        <div className="mx-auto max-w-5xl space-y-4 p-4">
          {posts.length > 0 ? (
            posts.map((post) => (
              <div
                key={post.postId}
                data-id={post.postId}
                className="post-card transform cursor-pointer rounded-lg bg-white p-4 shadow-md transition duration-300 hover:-translate-y-1 hover:scale-[1.01] hover:shadow-lg"
                onClick={() => navigate(`${post.postId}`)}
              >
                <h3 className="mb-2 text-base font-semibold text-gray-900">
                  {post.title}
                </h3>

                <div className="mb-2 flex items-center gap-4 text-sm text-gray-500">
                  {post.commentCount > 0 && (
                    <div className="flex items-center gap-1">
                      <MessageCircle size={14} />
                      <span>{post.commentCount}</span>
                    </div>
                  )}
                  {post.attachmentCount > 0 && (
                    <div className="flex items-center gap-1">
                      <Paperclip size={14} />
                      <span>{post.attachmentCount}</span>
                    </div>
                  )}
                </div>

                <p className="text-sm text-gray-500">
                  <span className="text-sm font-medium text-gray-700">
                    {post.categoryName}
                  </span>
                  <span className="mx-1">·</span>
                  <span>
                    {new Date(post.createdAt).toLocaleDateString("ko-KR", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                    })}
                  </span>
                  <span className="mx-1">·</span>
                  <span>{post.nickname}</span>
                </p>
              </div>
            ))
          ) : (
            <div className="py-8 text-center text-gray-400">
              게시글이 없습니다.
            </div>
          )}
        </div>

        {pageInfo && (
          <div className="mt-6">
            <Pagination
              page={page}
              startPage={pageInfo.startPage}
              endPage={pageInfo.endPage}
              prev={pageInfo.prev}
              next={pageInfo.next}
              onPageChange={loadPosts}
            />
          </div>
        )}
      </div>
    </SectionLoading>
  );
}

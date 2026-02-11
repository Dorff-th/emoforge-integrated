import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { fetchPosts } from "@/features/post/api/postApi";
import type { PostDTO } from "../types/Post";
import type { PageResponse } from "../types/Common";
import Pagination from "@/features/post/components/Pagination";
import { useToast } from "@/shared/stores/useToast";
import NewPostButton from "@/shared/components/NewPostButton";
import { LayoutList, MessageCircle, Paperclip } from "lucide-react";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useUILoading } from "@/shared/stores/useUILoading";

export default function PostListPage() {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);

  const navigate = useNavigate();

  const { isAuthenticated } = useAuth();
  const toast = useToast();

  const loadPosts = async (pageNum: number) => {
    try {
      const data = await fetchPosts(pageNum, 10, "createdAt", "DESC");

      if (data) {
        setPosts(data.dtoList);
        setPageInfo(data);
        setPage(pageNum);
      }
    } catch {
      toast.error("게시글 로드 실패");
    }
  };

  useEffect(() => {
    loadPosts(1);
  }, []);

  useUILoading("user:posts:list", { duration: 150 });

  return (
    <SectionLoading scope="user:diary:search">
      <div className="mx-auto max-w-6xl px-5 bg-gray-100 rounded-xl p-6">
        <div className="flex items-center gap-2 mb-4">
          <LayoutList size={20} />
          <h1 className="text-lg font-semibold">Posts</h1>
        </div>

        {isAuthenticated && (
          <div className="mb-4 flex">
            <NewPostButton />
          </div>
        )}

        {/* 📌 게시글 카드 리스트 */}
        <div className="max-w-5xl mx-auto space-y-4 p-4">
          {posts.length > 0 ? (
            posts.map((post) => (
              <div
                key={post.postId}
                data-id={post.postId}
                className="post-card bg-white rounded-lg shadow-md p-4
                         hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01]
                         transform transition duration-300 cursor-pointer"
                onClick={() => navigate(`${post.postId}`)}
              >
                {/* 제목 */}
                <h3 className="text-base font-semibold text-gray-900 mb-2">
                  {post.title}
                </h3>

                {/* 댓글/첨부 */}
                <div className="flex items-center gap-4 text-sm text-gray-500 mb-2">
                  {post.commentCount > 0 && (
                    <div className="flex items-center gap-1">
                      <MessageCircle size={14} />
                      <span>{post.commentCount}</span>
                    </div>
                  )}
                  {post.attachmentCount > 0 && (
                    <div className="flex items-center gap-1">
                      <Paperclip size={14} />
                      <span>2</span>
                    </div>
                  )}
                </div>

                {/* 카테고리 · 날짜 · 닉네임 */}
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
            <div className="text-center text-gray-400 py-8">
              게시글이 없습니다.
            </div>
          )}
        </div>

        {isAuthenticated && (
          <div className="mt-6 flex">
            <NewPostButton size="sm" />
          </div>
        )}

        {/* 📌 페이징 컴포넌트 */}
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

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchAdminPosts } from "@/features/admin/api/adminPostApi";
import type { PostDTO } from "@/features/post/types/Post";
import type { PageResponse } from "@/features/post/types/Common";
import { useToast } from "@/shared/stores/useToast";
import Pagination from "@/features/post/components/Pagination";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useUILoading } from "@/shared/stores/useUILoading";

export default function AdminPostListPage() {
  const navigate = useNavigate();
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);

  const toast = useToast();
  const loadPosts = async (pageNum: number) => {
    try {
      const data = await fetchAdminPosts(pageNum, 10, "createdAt", "DESC");

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

  useUILoading("user:posts:admin:list", { duration: 150 });

  return (
    <SectionLoading scope="user:posts:admin:list">
      <div className="p-4">
        <h2 className="text-xl font-bold mb-4">게시판 관리</h2>
        {/* 목록 */}
        <table className="min-w-full bg-white border">
          <thead>
            <tr className="bg-gray-100 border-b">
              <th className="p-2 text-left">ID</th>
              <th className="p-2 text-left">카테고리</th>
              <th className="p-2 text-left">제목</th>
              <th className="p-2 text-left">작성자(닉네임)</th>
              <th className="p-2 text-left">작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post) => (
              <tr key={post.postId} className="border-b hover:bg-gray-50">
                <td className="border px-2 py-1">{post.postId}</td>
                <td className="border px-2 py-1">{post.categoryName}</td>
                <td className="border px-2 py-1">{post.title}</td>
                <td className="border px-2 py-1">{post.nickname}</td>
                <td className="border px-2 py-1">
                  {new Date(post.createdAt).toLocaleDateString("ko-KR", {
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                  })}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
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

import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  bulkDeleteAdminPosts,
  fetchAdminPosts,
  type AdminPostSearchType,
} from "@/features/admin/api/adminPostApi";
import AdminPostSearchBar from "@/features/admin/components/posts/AdminPostSearchBar";
import Pagination from "@/features/post/components/Pagination";
import type { PageResponse } from "@/features/post/types/Common";
import type { PostDTO } from "@/features/post/types/Post";
import ConfirmModal from "@/shared/components/ConfirmModal";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useToast } from "@/shared/stores/useToast";
import { useUILoading } from "@/shared/stores/useUILoading";

export default function AdminPostListPage() {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);
  const [searchType, setSearchType] = useState<AdminPostSearchType>("ALL");
  const [keywordInput, setKeywordInput] = useState("");
  const [appliedKeyword, setAppliedKeyword] = useState("");
  const [selectedPostIds, setSelectedPostIds] = useState<number[]>([]);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const navigate = useNavigate();

  const toast = useToast();

  const loadPosts = async (
    pageNum: number,
    activeSearchType: AdminPostSearchType = searchType,
    activeKeyword: string = appliedKeyword,
  ) => {
    try {
      const data = await fetchAdminPosts(
        pageNum,
        10,
        "createdAt",
        "DESC",
        activeSearchType,
        activeKeyword,
      );

      if (data) {
        const requestedPage = pageNum;
        const maxPage = Math.max(1, data.totalPages || 1);

        if (requestedPage > maxPage) {
          await loadPosts(maxPage, activeSearchType, activeKeyword);
          return;
        }

        setPosts(data.dtoList);
        setPageInfo(data);
        setPage(requestedPage);
        setSelectedPostIds([]);
      }
    } catch {
      toast.error("게시글 로드 실패");
    }
  };

  const handleSearch = () => {
    const trimmedKeyword = keywordInput.trim();
    setAppliedKeyword(trimmedKeyword);
    loadPosts(1, searchType, trimmedKeyword);
  };

  const isAllChecked = useMemo(() => {
    if (posts.length === 0) {
      return false;
    }

    return posts.every((post) => selectedPostIds.includes(post.postId));
  }, [posts, selectedPostIds]);

  const handleToggleAll = (checked: boolean) => {
    if (checked) {
      setSelectedPostIds(posts.map((post) => post.postId));
      return;
    }

    setSelectedPostIds([]);
  };

  const handleToggleRow = (postId: number, checked: boolean) => {
    if (checked) {
      setSelectedPostIds((prev) => [...prev, postId]);
      return;
    }

    setSelectedPostIds((prev) => prev.filter((id) => id !== postId));
  };

  const handleDeleteSelected = () => {
    if (selectedPostIds.length === 0) {
      toast.error("삭제할 게시글을 선택해주세요.");
      return;
    }

    setIsDeleteModalOpen(true);
  };

  const handleConfirmDeleteSelected = async () => {
    try {
      await bulkDeleteAdminPosts(selectedPostIds);
      toast.success("선택한 게시글을 삭제했습니다.");
      setIsDeleteModalOpen(false);
      await loadPosts(page, searchType, appliedKeyword);
    } catch {
      toast.error("선택 삭제에 실패했습니다.");
      setIsDeleteModalOpen(false);
    }
  };

  useEffect(() => {
    loadPosts(1);
  }, []);

  useUILoading("admin:posts:list", { duration: 150 });

  return (
    <SectionLoading scope="user:posts:admin:list">
      <div className="p-4">
        <h2 className="mb-4 text-xl font-bold">게시글 관리</h2>

        <AdminPostSearchBar
          searchType={searchType}
          keyword={keywordInput}
          onSearchTypeChange={setSearchType}
          onKeywordChange={setKeywordInput}
          onSearch={handleSearch}
        />

        <table className="min-w-full border bg-white">
          <thead>
            <tr className="border-b bg-gray-100">
              <th className="w-10 p-2 text-center">
                <input
                  type="checkbox"
                  checked={isAllChecked}
                  onChange={(e) => handleToggleAll(e.target.checked)}
                />
              </th>
              <th className="p-2 text-left">ID</th>
              <th className="p-2 text-left">카테고리</th>
              <th className="p-2 text-left">제목</th>
              <th className="p-2 text-left">작성자 닉네임</th>
              <th className="p-2 text-left">작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.length === 0 ? (
              <tr>
                <td
                  className="border px-2 py-10 text-center text-gray-500"
                  colSpan={6}
                >
                  조회된 데이터가 없습니다.
                </td>
              </tr>
            ) : (
              posts.map((post) => (
                <tr
                  key={post.postId}
                  className="border-b hover:bg-yellow-50 cursor-pointer"
                  onClick={() => navigate(`${post.postId}`)}
                >
                  <td
                    className="border px-2 py-1 text-center"
                    onClick={(e) => e.stopPropagation()}
                  >
                    <input
                      type="checkbox"
                      checked={selectedPostIds.includes(post.postId)}
                      onChange={(e) =>
                        handleToggleRow(post.postId, e.target.checked)
                      }
                    />
                  </td>
                  <td className="border px-2 py-1">{post.postId}</td>
                  <td className="border px-2 py-1">{post.categoryName}</td>
                  <td className="border px-2 py-1">{post.title}</td>
                  <td className="border px-2 py-1">{post.nickname}</td>
                  <td className="border px-2 py-1">
                    {post.createdAt
                      ? new Date(post.createdAt).toLocaleString("ko-KR")
                      : "-"}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        <div className="mt-3">
          <button
            type="button"
            className="h-10 rounded bg-red-600 px-4 text-sm font-medium text-white disabled:cursor-not-allowed disabled:bg-gray-400"
            onClick={handleDeleteSelected}
            disabled={selectedPostIds.length === 0}
          >
            선택 삭제
          </button>
        </div>

        {pageInfo && (
          <div className="mt-6">
            <Pagination
              page={page}
              startPage={pageInfo.startPage}
              endPage={pageInfo.endPage}
              prev={pageInfo.prev}
              next={pageInfo.next}
              onPageChange={(targetPage) =>
                loadPosts(targetPage, searchType, appliedKeyword)
              }
            />
          </div>
        )}
      </div>

      <ConfirmModal
        open={isDeleteModalOpen}
        title="선택 삭제"
        description={`선택한 게시글 ${selectedPostIds.length}개를 삭제하시겠습니까?`}
        onConfirm={handleConfirmDeleteSelected}
        onCancel={() => setIsDeleteModalOpen(false)}
      />
    </SectionLoading>
  );
}

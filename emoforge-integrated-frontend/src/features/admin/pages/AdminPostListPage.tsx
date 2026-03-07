import { useEffect, useMemo, useState } from "react";
import {
  bulkDeleteAdminPosts,
  fetchAdminPosts,
  type AdminPostSearchType,
} from "@/features/admin/api/adminPostApi";
import type { PostDTO } from "@/features/post/types/Post";
import type { PageResponse } from "@/features/post/types/Common";
import { useToast } from "@/shared/stores/useToast";
import Pagination from "@/features/post/components/Pagination";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { useUILoading } from "@/shared/stores/useUILoading";
import ConfirmModal from "@/shared/components/ConfirmModal";

export default function AdminPostListPage() {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);
  const [searchType, setSearchType] = useState<AdminPostSearchType>("ALL");
  const [keywordInput, setKeywordInput] = useState("");
  const [appliedKeyword, setAppliedKeyword] = useState("");
  const [selectedPostIds, setSelectedPostIds] = useState<number[]>([]);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

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

  useUILoading("user:posts:admin:list", { duration: 150 });

  return (
    <SectionLoading scope="user:posts:admin:list">
      <div className="p-4">
        <h2 className="text-xl font-bold mb-4">게시글 관리</h2>

        <div className="mb-4 flex flex-wrap items-center gap-2">
          <select
            className="h-10 rounded border px-3 text-sm"
            value={searchType}
            onChange={(e) => setSearchType(e.target.value as AdminPostSearchType)}
          >
            <option value="ALL">전체</option>
            <option value="TITLE">제목</option>
            <option value="CONTENT">내용</option>
          </select>
          <input
            type="text"
            className="h-10 w-64 rounded border px-3 text-sm"
            placeholder="검색어를 입력하세요"
            value={keywordInput}
            onChange={(e) => setKeywordInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSearch();
              }
            }}
          />
          <button
            type="button"
            className="h-10 rounded bg-gray-900 px-4 text-sm font-medium text-white"
            onClick={handleSearch}
          >
            검색
          </button>
        </div>

        <table className="min-w-full bg-white border">
          <thead>
            <tr className="bg-gray-100 border-b">
              <th className="p-2 text-center w-10">
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
            {posts.map((post) => (
              <tr key={post.postId} className="border-b hover:bg-gray-50">
                <td className="border px-2 py-1 text-center">
                  <input
                    type="checkbox"
                    checked={selectedPostIds.includes(post.postId)}
                    onChange={(e) => handleToggleRow(post.postId, e.target.checked)}
                  />
                </td>
                <td className="border px-2 py-1">{post.postId}</td>
                <td className="border px-2 py-1">{post.categoryName}</td>
                <td className="border px-2 py-1">{post.title}</td>
                <td className="border px-2 py-1">{post.nickname}</td>
                <td className="border px-2 py-1">
                  {post.createdAt ? new Date(post.createdAt).toLocaleString("ko-KR") : "-"}
                </td>
              </tr>
            ))}
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
              onPageChange={(targetPage) => loadPosts(targetPage, searchType, appliedKeyword)}
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

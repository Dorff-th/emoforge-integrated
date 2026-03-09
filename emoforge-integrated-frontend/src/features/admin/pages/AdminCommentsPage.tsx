import { useEffect, useMemo, useState } from "react";
import {
  bulkDeleteAdminComments,
  fetchAdminComments,
  type AdminCommentSearchType,
} from "@/features/admin/api/adminPostApi";
import type { AdminCommentResponse } from "@/features/post/types/Comment";
import { useToast } from "@/shared/stores/useToast";
import type { PageResponse } from "@/features/post/types/Common";
import Pagination from "@/features/post/components/Pagination";
import { useUILoading } from "@/shared/stores/useUILoading";
import { SectionLoading } from "@/shared/components/SectionLoading";
import ConfirmModal from "@/shared/components/ConfirmModal";
import AdminCommentSearchBar from "@/features/admin/components/posts/AdminCommentSearchBar";

export default function AdminCommentsPage() {
  const [comments, setComments] = useState<AdminCommentResponse[]>([]);
  const [selectedCommentIds, setSelectedCommentIds] = useState<number[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const [pageInfo, setPageInfo] =
    useState<PageResponse<AdminCommentResponse> | null>(null);
  const [page, setPage] = useState(1);
  const [searchType, setSearchType] = useState<AdminCommentSearchType>("ALL");
  const [keywordInput, setKeywordInput] = useState("");
  const [appliedKeyword, setAppliedKeyword] = useState("");
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const toast = useToast();

  const loadComments = async (
    pageNum: number,
    activeSearchType: AdminCommentSearchType = searchType,
    activeKeyword: string = appliedKeyword,
  ) => {
    setIsLoading(true);
    try {
      const data = await fetchAdminComments(
        pageNum,
        15,
        activeSearchType,
        activeKeyword,
      );

      if (data) {
        const requestedPage = pageNum;
        const maxPage = Math.max(1, data.totalPages || 1);

        if (requestedPage > maxPage) {
          await loadComments(maxPage, activeSearchType, activeKeyword);
          return;
        }

        setComments(data.dtoList);
        setPageInfo(data);
        setPage(requestedPage);
        setSelectedCommentIds([]);
      }
    } catch (error) {
      console.error(error);
      setComments([]);
      setSelectedCommentIds([]);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadComments(1);
  }, []);

  const isAllChecked = useMemo(() => {
    if (comments.length === 0) return false;
    return comments.every((comment) =>
      selectedCommentIds.includes(comment.commentId),
    );
  }, [comments, selectedCommentIds]);

  const handleToggleAll = (checked: boolean) => {
    if (checked) {
      setSelectedCommentIds(comments.map((comment) => comment.commentId));
      return;
    }
    setSelectedCommentIds([]);
  };

  const handleToggleRow = (commentId: number, checked: boolean) => {
    if (checked) {
      setSelectedCommentIds((prev) => [...prev, commentId]);
      return;
    }
    setSelectedCommentIds((prev) => prev.filter((id) => id !== commentId));
  };

  const handleBulkDelete = () => {
    if (selectedCommentIds.length === 0) {
      toast.error("삭제할 댓글을 선택해주세요.");
      return;
    }

    setIsDeleteModalOpen(true);
  };

  const handleSearch = () => {
    const trimmedKeyword = keywordInput.trim();
    setAppliedKeyword(trimmedKeyword);
    loadComments(1, searchType, trimmedKeyword);
  };

  const handleConfirmDeleteSelected = async () => {
    setIsDeleting(true);
    try {
      await bulkDeleteAdminComments(selectedCommentIds);
      setComments((prev) =>
        prev.filter(
          (comment) => !selectedCommentIds.includes(comment.commentId),
        ),
      );
      setSelectedCommentIds([]);
      toast.success("선택한 댓글을 삭제했습니다.");
      setIsDeleteModalOpen(false);
    } catch (error) {
      console.error(error);
      toast.error("댓글 삭제에 실패했습니다.");
      setIsDeleteModalOpen(false);
    } finally {
      setIsDeleting(false);
    }
  };

  useUILoading("admin:comments", { duration: 150 });

  return (
    <SectionLoading scope="admin:comments">
      <div className="p-4">
        <h2 className="mb-4 text-xl font-bold">게시판 댓글 관리</h2>

        <AdminCommentSearchBar
          searchType={searchType}
          keyword={keywordInput}
          onSearchTypeChange={setSearchType}
          onKeywordChange={setKeywordInput}
          onSearch={handleSearch}
        />

        <table className="min-w-full border bg-white">
          <thead>
            <tr className="border-b bg-gray-100 ">
              <th className="w-10 p-2 text-center">
                <input
                  type="checkbox"
                  checked={isAllChecked}
                  onChange={(e) => handleToggleAll(e.target.checked)}
                  disabled={comments.length === 0 || isLoading || isDeleting}
                />
              </th>
              <th className="p-2 text-left">댓글 ID</th>
              <th className="p-2 text-left">내용</th>
              <th className="p-2 text-left">작성자</th>
              <th className="p-2 text-left">게시글제목</th>
              <th className="p-2 text-left">작성일</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? (
              <tr>
                <td
                  className="border px-2 py-6 text-center text-gray-500"
                  colSpan={6}
                >
                  댓글을 조회 중입니다.
                </td>
              </tr>
            ) : comments.length === 0 ? (
              <tr>
                <td
                  className="border px-2 py-10 text-center text-gray-500"
                  colSpan={6}
                >
                  조회된 댓글이 없습니다
                </td>
              </tr>
            ) : (
              comments.map((comment) => (
                <tr
                  key={comment.commentId}
                  className="border-b hover:bg-yellow-50"
                >
                  <td className="border px-2 py-1 text-center">
                    <input
                      type="checkbox"
                      checked={selectedCommentIds.includes(comment.commentId)}
                      onChange={(e) =>
                        handleToggleRow(comment.commentId, e.target.checked)
                      }
                      disabled={isDeleting || isLoading}
                    />
                  </td>
                  <td className="border px-2 py-1">{comment.commentId}</td>
                  <td className="border px-2 py-1">{comment.content}</td>
                  <td className="border px-2 py-1">{comment.author}</td>
                  <td className="border px-2 py-1">{comment.postTitle}</td>
                  <td className="border px-2 py-1">
                    {comment.createdAt
                      ? new Date(comment.createdAt).toLocaleString("ko-KR")
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
            onClick={handleBulkDelete}
            disabled={
              selectedCommentIds.length === 0 || isDeleting || isLoading
            }
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
                loadComments(targetPage, searchType, appliedKeyword)
              }
            />
          </div>
        )}
      </div>
      <ConfirmModal
        open={isDeleteModalOpen}
        title="선택 댓글 삭제"
        description={`선택한 댓글 ${selectedCommentIds.length}개를 삭제하시겠습니까?`}
        onConfirm={handleConfirmDeleteSelected}
        onCancel={() => setIsDeleteModalOpen(false)}
      />
    </SectionLoading>
  );
}

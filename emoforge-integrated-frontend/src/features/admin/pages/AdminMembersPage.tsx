import { useEffect, useState } from "react";
import {
  useDeleteMember,
  useMembers,
  useToggleMemberDeleted,
  useToggleMemberStatus,
} from "@/features/admin/hooks/useMembers";
import { DeleteTogglePill } from "@/features/admin/components/member/DeleteTogglePill";
import MemberSearchBar, {
  type MemberDeletedFilter,
} from "@/features/admin/components/member/MemberSearchBar";
import MemberDeleteButton from "../components/member/MemberDeleteButton";
import { StatusPill } from "@/features/admin/components/ui/StatusPill";
import Pagination from "@/features/post/components/Pagination";
import { formatDate, formatWithdrawInfo } from "@/shared/utils/dateUtils";
import { useToast } from "@/shared/stores/useToast";

const PAGE_SIZE = 10;
const MEMBER_SORT = "created_at";
const MEMBER_DIRECTION = "DESC" as const;

export default function AdminMembersPage() {
  const [page, setPage] = useState(1);
  const [nickname, setNickname] = useState("");
  const [deletedFilter, setDeletedFilter] =
    useState<MemberDeletedFilter>("ALL");
  const [appliedNickname, setAppliedNickname] = useState("");
  const [appliedDeleted, setAppliedDeleted] = useState<boolean>();

  const toast = useToast();
  const toggleStatusMutation = useToggleMemberStatus();
  const toggleDeletedMutation = useToggleMemberDeleted();
  const deleteMemberMutation = useDeleteMember();

  const {
    data: pageInfo,
    isLoading,
    isFetching,
    isError,
  } = useMembers({
    page,
    size: PAGE_SIZE,
    sort: MEMBER_SORT,
    direction: MEMBER_DIRECTION,
    nickname: appliedNickname,
    deleted: appliedDeleted,
  });

  const members = pageInfo?.dtoList ?? [];

  useEffect(() => {
    if (isError) {
      toast.error("회원 목록 로드 실패");
    }
  }, [isError, toast]);

  useEffect(() => {
    if (!pageInfo) {
      return;
    }

    const maxPage = Math.max(1, pageInfo.totalPages || 1);
    if (page > maxPage) {
      setPage(maxPage);
    }
  }, [page, pageInfo]);

  const handleSearch = () => {
    setAppliedNickname(nickname.trim());
    setAppliedDeleted(
      deletedFilter === "ALL"
        ? undefined
        : deletedFilter === "DELETED",
    );
    setPage(1);
  };

  const handleDelete = (uuid: string) => {
    const message = `
    정말 회원을 완전 삭제하시겠습니까?

    삭제되는 데이터
    - 게시글
    - 댓글
    - 첨부파일
    - 감정기록
    - 음악추천 데이터

    이 작업은 되돌릴 수 없습니다.
      `;

    if (!confirm(message)) return;

    deleteMemberMutation.mutate(uuid);
  };

  return (
    <div>
      <h2 className="text-lg font-bold mb-4">회원 관리</h2>
      <MemberSearchBar
        nickname={nickname}
        deletedFilter={deletedFilter}
        onNicknameChange={setNickname}
        onDeletedFilterChange={setDeletedFilter}
        onSearch={handleSearch}
      />
      <table className="min-w-full bg-white border">
        <thead className="bg-gray-100 border-b text-center">
          <tr>
            <th className="p-2">UUID</th>
            <th>닉네임</th>
            <th>상태</th>
            <th>탈퇴여부</th>
            <th>가입일</th>
            <th>마지막 로그인</th>
            <th>탈퇴일</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody className="text-center">
          {members.map((m) => {
            const isAdmin = m.role === "ADMIN";
            const isStatusLoading =
              toggleStatusMutation.isPending &&
              toggleStatusMutation.variables?.uuid === m.uuid;
            const isDeletedLoading =
              toggleDeletedMutation.isPending &&
              toggleDeletedMutation.variables?.uuid === m.uuid;

            return (
              <tr key={m.uuid} className="border-b hover:bg-gray-50">
                <td className="p-2 font-mono text-sm">{m.uuid}</td>
                <td className="p-2">
                  {m.nickname}
                  {isAdmin && (
                    <span className="ml-2 text-xs px-2 py-0.5 bg-purple-100 text-purple-700 rounded">
                      ADMIN
                    </span>
                  )}
                </td>
                <td className="p-2">
                  {!isAdmin ? (
                    <StatusPill
                      label={m.status === "ACTIVE" ? "활성" : "비활성"}
                      state={m.status === "ACTIVE" ? "active" : "inactive"}
                      onClick={() =>
                        toggleStatusMutation.mutate({
                          uuid: m.uuid,
                          currentStatus: m.status,
                        })
                      }
                      isLoading={isStatusLoading}
                      ariaLabel={`${m.username} 상태: ${m.status}. 클릭하여 전환`}
                    />
                  ) : (
                    <span className="text-gray-400 text-sm">관리자</span>
                  )}
                </td>
                <td className="p-2">
                  {!isAdmin ? (
                    <DeleteTogglePill
                      isDeleted={m.deleted}
                      onToggle={() =>
                        toggleDeletedMutation.mutate({
                          uuid: m.uuid,
                          currentDeleted: m.deleted,
                        })
                      }
                      isLoading={isDeletedLoading}
                      memberName={m.username}
                    />
                  ) : (
                    <span className="text-gray-400 text-sm">-</span>
                  )}
                </td>
                <td>{formatDate(m.createdAt)}</td>
                <td>{formatDate(m.lastLoginAt)}</td>
                <td>{m.deleted ? formatWithdrawInfo(m.deletedAt) : "-"}</td>
                <td className="p-2">
                  <MemberDeleteButton
                    isAdmin={isAdmin}
                    deleted={m.deleted}
                    uuid={m.uuid}
                    onDelete={handleDelete}
                  />
                </td>
              </tr>
            );
          })}
          {!isLoading && members.length === 0 && (
            <tr>
              <td colSpan={8} className="p-6 text-sm text-gray-500">
                조회된 회원이 없습니다.
              </td>
            </tr>
          )}
        </tbody>
      </table>
      {isFetching && (
        <div className="mt-2 text-sm text-gray-500">
          목록을 불러오는 중입니다.
        </div>
      )}
      {pageInfo && (
        <div className="mt-6">
          <Pagination
            page={page}
            startPage={pageInfo.startPage}
            endPage={pageInfo.endPage}
            prev={pageInfo.prev}
            next={pageInfo.next}
            onPageChange={setPage}
          />
        </div>
      )}
    </div>
  );
}

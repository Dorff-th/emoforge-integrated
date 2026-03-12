import {
  useMembers,
  useToggleMemberStatus,
  useToggleMemberDeleted,
  useDeleteMember,
} from "@/features/admin/hooks/useMembers";
import { StatusPill } from "@/features/admin/components/ui/StatusPill";
import { DeleteTogglePill } from "@/features/admin/components/member/DeleteTogglePill";
import MemberDeleteButton from "../components/member/MemberDeleteButton";
import { formatWithdrawInfo, formatDate } from "@/shared/utils/dateUtils";

export default function AdminMembersPage() {
  const { data: members = [], isLoading } = useMembers();
  const toggleStatusMutation = useToggleMemberStatus();
  const toggleDeletedMutation = useToggleMemberDeleted();

  const deleteMemberMutation = useDeleteMember();

  const handleDelete = (uuid: string) => {
    const message = `
    정말 회원을 완전히 삭제하시겠습니까?

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

  if (isLoading) {
    return <div className="p-4">회원 목록을 불러오는 중...</div>;
  }

  return (
    <div>
      <h2 className="text-lg font-bold mb-4">회원 관리</h2>
      <table className="min-w-full bg-white border">
        <thead className="bg-gray-100 border-b text-center">
          <tr>
            <th className="p-2">UUID</th>
            <th>닉네임</th>
            <th>상태</th>
            <th>탈퇴여부</th>
            <th>가입일</th>
            <th>마지막로그인</th>
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
                      ariaLabel={`${m.username} 상태: ${m.status}. 클릭하여 토글`}
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
        </tbody>
      </table>
    </div>
  );
}

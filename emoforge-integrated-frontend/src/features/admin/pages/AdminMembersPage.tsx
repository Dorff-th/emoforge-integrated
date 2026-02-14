import {
  useMembers,
  useToggleMemberStatus,
  useToggleMemberDeleted,
} from "@/features/admin/hooks/useMembers";
import { StatusPill } from "@/features/admin/components/ui/StatusPill";
import { DeleteTogglePill } from "@/features/admin/components/member/DeleteTogglePill";

export default function AdminMembersPage() {
  const { data: members = [], isLoading } = useMembers();
  const toggleStatusMutation = useToggleMemberStatus();
  const toggleDeletedMutation = useToggleMemberDeleted();

  if (isLoading) {
    return <div className="p-4">회원 목록을 불러오는 중...</div>;
  }

  return (
    <div>
      <h2 className="text-lg font-bold mb-4">회원 관리</h2>
      <table className="min-w-full bg-white border">
        <thead>
          <tr className="bg-gray-100 border-b">
            <th className="p-2 text-left">UUID</th>
            <th className="p-2 text-left">닉네임</th>
            <th className="p-2 text-left">상태</th>
            <th className="p-2 text-left">탈퇴여부</th>
          </tr>
        </thead>
        <tbody>
          {members.map((m) => {
            const isStatusLoading =
              toggleStatusMutation.isPending &&
              toggleStatusMutation.variables?.uuid === m.uuid;
            const isDeletedLoading =
              toggleDeletedMutation.isPending &&
              toggleDeletedMutation.variables?.uuid === m.uuid;

            return (
              <tr key={m.uuid} className="border-b hover:bg-gray-50">
                <td className="p-2 font-mono text-sm">{m.uuid}</td>
                <td className="p-2">{m.nickname}</td>
                <td className="p-2">
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
                </td>
                <td className="p-2">
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
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

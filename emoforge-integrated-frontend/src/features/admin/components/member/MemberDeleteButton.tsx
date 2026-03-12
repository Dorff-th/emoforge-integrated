import { Trash2 } from "lucide-react";

interface MemberDeleteButtonProps {
  isAdmin: boolean;
  deleted: boolean;
  uuid: string;
  onDelete: (uuid: string) => void;
}

export default function MemberDeleteButton({
  isAdmin,
  deleted,
  uuid,
  onDelete,
}: MemberDeleteButtonProps) {
  // 관리자 계정이면 삭제 불가
  if (isAdmin) {
    return <span className="text-gray-400 text-sm">-</span>;
  }

  return (
    <button
      disabled={!deleted}
      onClick={() => onDelete(uuid)}
      title={!deleted ? "탈퇴 회원만 삭제 가능합니다" : "회원 완전 삭제"}
      className={`inline-flex items-center gap-1 px-2 py-1 text-xs
        border rounded-md transition-colors
        ${
          deleted
            ? "text-red-600 border-red-300 hover:bg-red-50 hover:border-red-400 hover:text-red-700"
            : "text-gray-400 border-gray-300 cursor-not-allowed"
        }`}
    >
      <Trash2 size={14} />
      삭제
    </button>
  );
}

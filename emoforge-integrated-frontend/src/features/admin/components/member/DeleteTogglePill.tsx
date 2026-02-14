import { useState } from "react";
import { StatusPill } from "@/features/admin/components/ui/StatusPill";
import ConfirmModal from "@/shared/components/ConfirmModal";

interface DeleteTogglePillProps {
  isDeleted: boolean;
  onToggle: () => void;
  isLoading?: boolean;
  memberName?: string;
}

export function DeleteTogglePill({
  isDeleted,
  onToggle,
  isLoading = false,
  memberName = "회원",
}: DeleteTogglePillProps) {
  const [showConfirm, setShowConfirm] = useState(false);

  const handleClick = () => {
    if (!isDeleted) {
      // N -> Y (탈퇴 처리): 확인 모달 표시
      setShowConfirm(true);
    } else {
      // Y -> N (탈퇴 해제): 바로 실행 (안전한 작업)
      onToggle();
    }
  };

  const handleConfirm = () => {
    setShowConfirm(false);
    onToggle();
  };

  return (
    <>
      <StatusPill
        label={isDeleted ? "탈퇴" : "정상"}
        state={isDeleted ? "deleted" : "safe"}
        onClick={handleClick}
        isLoading={isLoading}
        ariaLabel={`탈퇴 상태: ${isDeleted ? "탈퇴됨" : "정상"}. 클릭하여 토글`}
      />
      <ConfirmModal
        open={showConfirm}
        title="회원 탈퇴 처리"
        description={`'${memberName}' 회원을 탈퇴 처리하시겠습니까? 탈퇴된 회원은 서비스를 이용할 수 없습니다.`}
        onConfirm={handleConfirm}
        onCancel={() => setShowConfirm(false)}
      />
    </>
  );
}

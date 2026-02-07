import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Trash2 } from "lucide-react";
import { useToast } from "@/shared/stores/useToast";
import { useWithdrawalMutation } from "@/features/user/hooks/useWithdrawal";
import { useLogoutMutation } from "@/features/user/hooks/useLogout";
import ConfirmModal from "@/shared/components/ConfirmModal";

export default function ProfileWithdrawalSection() {
  const [withdrawalTarget, setWithdrawalTarget] = useState<any | null>(null);

  const toast = useToast();
  const navigate = useNavigate();

  const withdrawalMutation = useWithdrawalMutation();
  const logoutMutation = useLogoutMutation();

  const handleCancelWithdrawal = () => {
    setWithdrawalTarget(false);
  };

  const handleWithdrawalConfirm = async () => {
    if (!withdrawalTarget) return;

    try {
      await withdrawalMutation.mutateAsync();

      toast.success(
        "탈퇴 신청이 완료되었습니다.\n다시 로그인하려면 탈퇴 취소가 필요합니다.",
      );

      await logoutMutation.mutateAsync();

      navigate("/login");
    } catch (err) {
      toast.error("탈퇴 신청 중 오류가 발생했습니다.");
      console.error(err);
    } finally {
      setWithdrawalTarget(null);
    }
  };

  return (
    <section className="space-y-3">
      <div className="mt-6 text-center">
        <button
          className="
              inline-flex items-center gap-2
              text-xs text-red-500
            hover:text-red-600 hover:underline"
          onClick={() => setWithdrawalTarget(true)}
        >
          <Trash2 size={14} />
          Delete Account
        </button>
      </div>
      <ConfirmModal
        open={!!withdrawalTarget}
        title="회원탈퇴 확인"
        description={
          withdrawalTarget
            ? `정말 회원 탈퇴를 진행하시겠습니까?\n탈퇴 신청 후 10일 뒤 모든 데이터가 삭제됩니다.`
            : ""
        }
        onConfirm={handleWithdrawalConfirm}
        onCancel={handleCancelWithdrawal}
      />
    </section>
  );
}

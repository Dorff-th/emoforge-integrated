import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { Modal } from "@/shared/components/Modal";
import { checkEmail, updateEmail } from "@/features/user/api/profile.api";

interface EmailEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  initialEmail: string;
}

const EmailEditModal = ({
  isOpen,
  onClose,
  initialEmail,
}: EmailEditModalProps) => {
  const [email, setEmail] = useState(initialEmail);
  const [isChecking, setIsChecking] = useState(false);
  const [isAvailable, setIsAvailable] = useState<boolean | null>(null);
  const [valid, setVaild] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const queryClient = useQueryClient();

  //이메일 형식 검증
  const isValidEmail = (value: string) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

  //이메일 중복 체크 로직
  const handleCheckEmail = async () => {
    if (!email.trim()) {
      setError("이메일을 입력해주세요.");
      return;
    }

    if (!isValidEmail(email)) {
      setError("올바른 이메일 형식이 아닙니다.");
      return;
    }

    try {
      setIsChecking(true);
      setError(null);

      const res = await checkEmail(email);
      if (res.data.available) {
        setIsAvailable(true);
        setVaild("사용 가능한 이메일입니다.");
      } else {
        setIsAvailable(false);
        setError("이미 사용 중인 이메일입니다.");
      }

      setIsAvailable(true);
    } catch (e) {
      setIsAvailable(false);
      setError("이메일 중복 확인에 실패했습니다.");
    } finally {
      setIsChecking(false);
    }
  };

  //이메일 저장 로직
  const handleSave = async () => {
    if (!isAvailable) {
      //setError("이메일 중복 확인을 해주세요.");

      return;
    }

    try {
      await updateEmail(email);

      await queryClient.invalidateQueries({
        queryKey: ["auth", "me"],
      });

      onClose();
    } catch (e) {
      //setError("이메일 변경에 실패했습니다.");
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="이메일 변경">
      <div className="space-y-4">
        <input
          type="email"
          value={email}
          onChange={(e) => {
            setEmail(e.target.value);
            setIsAvailable(null); // 🔥 값 바뀌면 다시 체크 필요
          }}
          className="w-full rounded border px-3 py-2"
          placeholder="example@email.com"
        />

        <button
          onClick={handleCheckEmail}
          disabled={isChecking}
          className="btn-secondary"
        >
          중복 확인
        </button>

        {error && <p className="text-red-500">{error}</p>}
        {valid && <p className="text-green-500">{valid}</p>}

        <div className="flex justify-end gap-2">
          <button onClick={onClose} className="btn-ghost">
            취소
          </button>
          <button onClick={handleSave} className="btn-primary">
            저장
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default EmailEditModal;

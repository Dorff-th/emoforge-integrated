import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { Modal } from "@/shared/components/Modal";
import { checkNickname, updateNickname } from "@/features/user/api/profile.api";

interface NicknameEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  initialNickname: string;
}

const NickNameEditModal = ({
  isOpen,
  onClose,
  initialNickname,
}: NicknameEditModalProps) => {
  const [nickname, setNickname] = useState(initialNickname);
  const [isChecking, setIsChecking] = useState(false);
  const [isAvailable, setIsAvailable] = useState<boolean | null>(null);
  const [valid, setVaild] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  //nickname 중복 체크 로직
  const handleCheckNickname = async () => {
    if (!nickname.trim()) {
      setError("닉네임을 입력해주세요.");
      return;
    }

    try {
      setIsChecking(true);
      setError(null);

      const res = await checkNickname(nickname);
      if (res.data.available) {
        setIsAvailable(true);
        setVaild("사용 가능한 닉네임입니다.");
      } else {
        setIsAvailable(false);
        setError("이미 사용 중인 닉네임입니다.");
      }

      setIsAvailable(true);
    } catch (e) {
      setIsAvailable(false);
      setError("이미 사용 중인 닉네임입니다.");
    } finally {
      setIsChecking(false);
    }
  };

  const queryClient = useQueryClient();

  const handleSave = async () => {
    if (!isAvailable) {
      setError("닉네임 중복 확인을 해주세요.");
      return;
    }

    try {
      await updateNickname(nickname);

      await queryClient.invalidateQueries({
        queryKey: ["auth", "me"],
      });

      onClose();
    } catch (e) {
      setError("닉네임 변경에 실패했습니다.");
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="닉네임 변경">
      <div className="space-y-4">
        <input
          value={nickname}
          onChange={(e) => {
            setNickname(e.target.value);
            setIsAvailable(null); // 🔥 입력 바뀌면 다시 체크 필요
          }}
          className="w-full border px-3 py-2 rounded"
        />

        <button
          onClick={handleCheckNickname}
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

export default NickNameEditModal;

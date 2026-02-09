import React from "react";
import toastChar3 from '@/assets/toast/toast_char_3.png';

interface ConfirmDialogProps {
  open: boolean;
  title?: string;
  message: string;
  imageSrc?: string;
  loading?: boolean; // ✅ 추가
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmDialog: React.FC<ConfirmDialogProps> = ({
  open,
  title = "확인",
  message,
  imageSrc = toastChar3, // ✅ 토끼 이미지 (public 폴더에 넣기)
  loading,
  onConfirm,
  onCancel,
}) => {
  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/40 flex justify-center items-center z-[100]">
      <div className="bg-white rounded-2xl shadow-lg p-6 w-[320px] flex flex-col items-center animate-fadeIn">
        {/* 🐰 토끼 이미지 */}
        <img src={imageSrc} alt="GPT Warning" className="w-20 h-20 mb-3" />

        {/* 제목 */}
        <h3 className="text-lg font-semibold mb-2 text-gray-800">{title}</h3>

        {/* 메시지 */}
        <p className="text-sm text-gray-600 text-center mb-4 leading-relaxed">
          {message}
        </p>

        {/* 버튼 영역 */}
        <div className="flex gap-3 mt-2">
          <button
            onClick={onCancel}
            disabled={loading} // ✅ 중복 클릭 방지
            className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300 transition text-sm"
          >
            취소
          </button>
          <button
            onClick={onConfirm}
            disabled={loading} // ✅ 로딩 중에는 취소도 막을 수 있음 (선택)
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition text-sm"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmDialog;
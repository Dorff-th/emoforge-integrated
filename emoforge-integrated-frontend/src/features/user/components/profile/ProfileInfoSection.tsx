import { useState } from "react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { Trash2, Pencil, Mail, UserPlus, Edit3 } from "lucide-react";
import NickNameEditModal from "./modals/NicknameEditModal";
import EmailEditModal from "./modals/EmailEditModal";
export default function ProfileInfoSection() {
  const { user } = useAuth();

  const [openNicknameModal, setOpenNicknameModal] = useState(false);
  const [openEmailModal, setOpenEmailModal] = useState(false);

  return (
    <section className="space-y-4">
      {/* 닉네임 */}
      <div className="flex items-center gap-2">
        <h2 className="text-2xl font-semibold">{user?.data.nickname}</h2>
        <button
          className="text-sm text-blue-500 hover:underline"
          onClick={() => setOpenNicknameModal(true)}
        >
          <Pencil size={16} />
        </button>
      </div>

      {/* 이메일 */}
      <div className="flex items-center gap-2 text-gray-600">
        <Mail size={16} className="text-gray-600" />
        <span className="leading-none">{user?.data.email}</span>

        <button
          className="text-sm text-blue-500 hover:underline"
          onClick={() => setOpenEmailModal(true)}
        >
          <Pencil size={16} />
        </button>
      </div>

      {/* 가입일 / 정보 변경일 */}
      <div className="mt-6 space-y-2 text-xs text-gray-500">
        <div className="flex items-center gap-2">
          <UserPlus size={14} className="text-gray-400" />
          <span>Joined on</span>
          <span className="ml-auto text-gray-400">{user?.data.createdAt}</span>
        </div>

        <div className="flex items-center gap-2">
          <Edit3 size={14} className="text-gray-400" />
          <span>Last updated</span>
          <span className="ml-auto text-gray-400">{user?.data.updatedAt}</span>
        </div>
      </div>
      {/* ============================= */}
      {/* 📌 모달 영역 */}
      {/* ============================= */}
      {openNicknameModal && (
        <NickNameEditModal
          isOpen={openNicknameModal}
          initialNickname={user?.data.nickname || ""}
          onClose={() => setOpenNicknameModal(false)}
        />
      )}
      {openEmailModal && (
        <EmailEditModal
          isOpen={openEmailModal}
          initialEmail={user?.data.email || ""}
          onClose={() => setOpenEmailModal(false)}
        />
      )}
    </section>
  );
}

import { useState } from "react";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useProfileImage } from "@/features/user/hooks/useProfileImage";
import ProfileImageUploadModal from "./modals/ProfileImageUploadModal";
import { Avatar } from "@/shared/components/Avatar";
import { Settings } from "lucide-react";

const ProfileImageSection = () => {
  const { user } = useAuth();
  const { publicUrl, upload, isUploading } = useProfileImage(user?.uuid);
  const [isModalOpen, setIsModalOpen] = useState(false);

  if (!user) return null;

  const handleUpload = (file: File) => {
    upload(
      { file },
      {
        onSuccess: () => {
          setIsModalOpen(false); // ✅ 여기서 닫기
        },
      },
    );
  };

  return (
    <section className="flex flex-col items-center gap-3">
      <div className="relative">
        <Avatar publicUrl={publicUrl} name={user.nickname} size={96} />

        {/* Gear Button */}
        <button
          type="button"
          onClick={() => setIsModalOpen(true)}
          className="
            absolute bottom-0 right-0
            flex h-8 w-8 items-center justify-center
            rounded-full
            bg-[var(--surface)]
            border border-[var(--border)]
            shadow-sm
            hover:bg-[var(--border)]
            transition
          "
        >
          <Settings size={16} />
        </button>
      </div>
      <ProfileImageUploadModal
        open={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        isUploading={isUploading}
        //onUpload={(file) => upload({ file })}
        onUpload={handleUpload}
      />
    </section>
  );
};

export default ProfileImageSection;

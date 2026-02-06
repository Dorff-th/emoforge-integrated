import { useRef, useState } from "react";
import { Avatar } from "@/shared/components/Avatar";

interface ProfileImageUploadModalProps {
  open: boolean;
  isUploading: boolean;
  onClose: () => void;
  onUpload: (file: File) => void;
}

export default function ProfileImageUploadModal({
  open,
  isUploading,
  onClose,
  onUpload,
}: ProfileImageUploadModalProps) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [file, setFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  if (!open) return null;

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0];
    if (!selected) return;

    setFile(selected);
    setPreviewUrl(URL.createObjectURL(selected));
  };

  const handleUpload = () => {
    if (!file) return;
    onUpload(file);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Backdrop */}
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />

      {/* Modal */}
      <div className="relative z-10 w-full max-w-sm rounded-lg bg-[var(--surface)] p-6 shadow-lg">
        <h2 className="text-lg font-semibold mb-4">프로필 이미지 변경</h2>

        {/* Preview */}
        <div className="flex justify-center mb-4">
          <Avatar publicUrl={previewUrl} name="preview" size={96} />
        </div>

        {/* File input */}
        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="hidden"
        />

        <button
          onClick={() => fileInputRef.current?.click()}
          className="
            w-full rounded-md
            border border-[var(--border)]
            px-3 py-2 text-sm
            hover:bg-[var(--border)]
          "
        >
          이미지 선택
        </button>

        {/* Actions */}
        <div className="mt-6 flex justify-end gap-2">
          <button
            onClick={onClose}
            disabled={isUploading}
            className="text-sm text-[var(--text-muted)]"
          >
            취소
          </button>

          <button
            onClick={handleUpload}
            disabled={!file || isUploading}
            className="
              rounded-md px-4 py-2 text-sm font-medium
              bg-[var(--primary)]
              text-white
              disabled:opacity-50
            "
          >
            {isUploading ? "업로드 중..." : "업로드"}
          </button>
        </div>
      </div>
    </div>
  );
}

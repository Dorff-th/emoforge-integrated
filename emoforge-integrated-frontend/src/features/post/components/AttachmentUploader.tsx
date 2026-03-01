import { useCallback, useRef } from "react";
import type { ChangeEvent } from "react";
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useToast } from "@/shared/stores/useToast";
import { formatFileSize } from "@/shared/utils/fileUtils";
import type { AttachmentItem } from "@/features/post/types/Attachment";
import { Paperclip } from "lucide-react";
import axios from "axios";

interface AttachmentUploaderProps {
  groupTempKey: string;
  items: AttachmentItem[];
  setItems: (items: AttachmentItem[]) => void;
  deleteIds: number[];
  setDeleteIds: (ids: number[]) => void;
}

const AttachmentUploader = ({
  groupTempKey,
  items,
  setItems,
  deleteIds,
  setDeleteIds,
}: AttachmentUploaderProps) => {
  const { user, isAuthenticated } = useAuth();
  const toast = useToast();

  const memberUuid = user?.uuid;

  const inputRef = useRef<HTMLInputElement | null>(null);

  const resetInput = useCallback(() => {
    if (inputRef.current) {
      inputRef.current.value = "";
    }
  }, []);

  const handleFileSelect = useCallback(
    async (event: ChangeEvent<HTMLInputElement>) => {
      const selectedFiles = event.target.files
        ? Array.from(event.target.files)
        : [];
      if (!selectedFiles.length) return;

      if (!isAuthenticated || !memberUuid) {
        toast.error("로그인이 필요합니다.");
        resetInput();
        return;
      }

      if (!groupTempKey) {
        toast.error("첨부파일 키가 없습니다.");
        resetInput();
        return;
      }

      const uploaded: AttachmentItem[] = [];

      for (const file of selectedFiles) {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("groupTempKey", groupTempKey);
        formData.append("tempKey", groupTempKey);
        formData.append("uploadType", "ATTACHMENT");
        formData.append("memberUuid", memberUuid);
        formData.append("attachmentStatus", "TEMP");

        try {
          const { data } = await http.post<AttachmentItem>(
            `${API.ATTACH}`,
            formData,
            {
              headers: { "Content-Type": "multipart/form-data" },
            },
          );

          uploaded.push({
            ...data,
            isNew: true,
            originalName:
              data.originalName ??
              data.originFileName ??
              data.fileName ??
              file.name,
            fileSize: data.fileSize ?? file.size,
          });
        } catch (error) {
          console.error("업로드 실패:", error);
          //2026.03.02 추가
          if (axios.isAxiosError(error)) {
            const message = error.response?.data?.message;

            //2026.03.02 추가
            switch (message) {
              case "INVALID_FILE_EXTENSION":
                toast.error("허용되지 않은 파일 형식입니다.");
                break;
              case "FILE_SIZE_EXCEEDED":
                toast.error("파일 용량이 허용 범위를 초과했습니다.");
                break;
              default:
                toast.error("업로드에 실패했습니다.");
            }
          }

          //toast.error(`파일 ${file.name} 업로드 실패`);
        }
      }

      if (uploaded.length) {
        setItems([...items, ...uploaded]);
      }

      resetInput();
    },
    [groupTempKey, memberUuid, items, setItems, resetInput],
  );

  const handleDelete = useCallback(
    async (att: AttachmentItem) => {
      if (att.isNew) {
        // 신규 첨부 → 즉시 삭제
        try {
          await http.delete(`${API.ATTACH}/${att.id}`);
          setItems(items.filter((a) => a.id !== att.id));
          toast.success("첨부파일이 삭제되었습니다.");
        } catch (error) {
          console.error("삭제 실패:", error);

          toast.error("첨부파일 삭제 실패");
        }
      } else {
        // 기존 첨부 → 삭제 예정 표시
        setItems(
          items.map((a) =>
            a.id === att.id ? { ...a, markedForDelete: true } : a,
          ),
        );
        if (!deleteIds.includes(att.id)) {
          setDeleteIds([...deleteIds, att.id]);
        }
      }
    },
    [items, setItems, deleteIds, setDeleteIds],
  );

  const handleUndoDelete = useCallback(
    (att: AttachmentItem) => {
      setItems(
        items.map((a) =>
          a.id === att.id ? { ...a, markedForDelete: false } : a,
        ),
      );
      setDeleteIds(deleteIds.filter((id) => id !== att.id));
    },
    [items, setItems, deleteIds, setDeleteIds],
  );

  return (
    <div className="space-y-3">
      <div>
        <label className="flex items-center gap-2">
          <Paperclip size={16} />
          Attachments
        </label>
        <p className="text-xs text-gray-500">
          Upload supporting files for this post.
        </p>
      </div>

      <input
        ref={inputRef}
        type="file"
        multiple
        onChange={handleFileSelect}
        className="block w-full cursor-pointer rounded border border-gray-300 px-3 py-2 text-sm"
      />

      {items.length > 0 && (
        <ul className="space-y-2">
          {items.map((att) => (
            <li
              key={att.id}
              className={`flex items-center justify-between rounded border px-3 py-2 text-sm shadow-sm ${
                att.markedForDelete
                  ? "bg-gray-100 line-through text-gray-400"
                  : "bg-white"
              }`}
            >
              <div className="min-w-0 pr-3">
                <p className="truncate font-medium">
                  {att.originFileName ?? att.fileName ?? "파일"}
                </p>
                {typeof att.fileSize === "number" && (
                  <p className="text-xs text-gray-500">
                    {formatFileSize(att.fileSize)}
                  </p>
                )}
              </div>

              {att.isNew ? (
                <button
                  type="button"
                  onClick={() => handleDelete(att)}
                  className="text-xs font-medium text-red-500 hover:text-red-600"
                >
                  삭제
                </button>
              ) : att.markedForDelete ? (
                <button
                  type="button"
                  onClick={() => handleUndoDelete(att)}
                  className="text-xs font-medium text-blue-500 hover:text-blue-600"
                >
                  삭제취소
                </button>
              ) : (
                <button
                  type="button"
                  onClick={() => handleDelete(att)}
                  className="text-xs font-medium text-red-500 hover:text-red-600"
                >
                  삭제
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default AttachmentUploader;

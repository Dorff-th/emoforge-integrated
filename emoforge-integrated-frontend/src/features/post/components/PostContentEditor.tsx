﻿import { forwardRef, useCallback, useEffect, useRef } from "react";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { useToast } from "@/shared/stores/useToast";
import { fixContentForEditor } from "@/shared/utils/contentUrlHelper";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";

interface Props {
  value: string;
  onChange: (val: string) => void;
  groupTempKey?: string;
  postId?: number; // edit 모드에서만 넘어오는 postId
}

interface AttachUploadResponseDto {
  id: number;
  fileName: string;
  originFileName: string;
  fileType: string;
  fileUrl?: string;
  publicUrl?: string;
  url?: string;
  createdAt: string;
}

const PostContentEditor = forwardRef<Editor, Props>(
  ({ value, onChange, groupTempKey, postId }, ref) => {
    //const dispatch = useAppDispatch();
    //const memberUuid = useAppSelector((state) => state.auth.user?.uuid);

    const { user, isAuthenticated } = useAuth();
    const memberUuid = user?.uuid;
    const toast = useToast();

    const tempKeyRef = useRef<string>(groupTempKey ?? uuidv4());

    const handleImageUpload = useCallback(
      async (blob: Blob, callback: (url: string, altText: string) => void) => {
        if (!isAuthenticated || !memberUuid) {
          toast.error("로그인이 필요합니다.");
          return false;
        }

        const formData = new FormData();

        formData.append("file", blob);
        formData.append("uploadType", "EDITOR_IMAGE");
        formData.append("memberUuid", memberUuid);
        formData.append("attachmentStatus", "TEMP");
        formData.append("tempKey", tempKeyRef.current);
        formData.append("groupTempKey", tempKeyRef.current);
        if (postId) {
          formData.append("postId", postId.toString());
        }

        try {
          const { data } = await http.post<AttachUploadResponseDto>(
            `${API.ATTACH}`,
            formData,
            {
              headers: { "Content-Type": "multipart/form-data" },
            },
          );

          const imageUrl = data.publicUrl ?? data.fileUrl ?? data.url;

          if (!imageUrl) {
            throw new Error(
              "Attachment service did not return an accessible URL.",
            );
          }

          const altText = (blob as File).name ?? "editor image";
          callback(imageUrl, altText);
        } catch (error) {
          console.error("Image upload failed:", error);
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

          //toast.error("이미지 업로드 실패");
        }

        return false;
      },
      [memberUuid],
    );

    useEffect(() => {
      if (!ref || typeof ref === "function") return;
      const editorInstance = ref.current?.getInstance();
      if (!editorInstance) return;

      if (typeof editorInstance.removeHook === "function") {
        editorInstance.removeHook("addImageBlobHook");
      }

      editorInstance.addHook("addImageBlobHook", handleImageUpload);

      return () => {
        if (typeof editorInstance.removeHook === "function") {
          editorInstance.removeHook("addImageBlobHook");
        }
      };
    }, [handleImageUpload, ref]);

    useEffect(() => {
      if (!ref || typeof ref === "function") return;
      const editorInstance = ref.current?.getInstance();
      if (!editorInstance) return;

      if (!value) {
        return;
      }

      const fixedContent = fixContentForEditor(value);
      if (editorInstance.getMarkdown() === fixedContent) {
        return;
      }

      editorInstance.setMarkdown(fixedContent);
    }, [ref, value]);

    const handleEditorChange = useCallback(() => {
      if (!ref || typeof ref === "function") return;
      const editorInstance = ref.current?.getInstance();
      if (!editorInstance) return;

      onChange(editorInstance.getMarkdown());
    }, [onChange, ref]);

    return (
      <div className="border rounded-lg overflow-hidden">
        <Editor
          ref={ref}
          height="400px"
          initialEditType="wysiwyg"
          previewStyle="vertical"
          placeholder="Write your content..."
          useCommandShortcut={true}
          onChange={handleEditorChange}
        />
      </div>
    );
  },
);

PostContentEditor.displayName = "PostContentEditor";

export default PostContentEditor;

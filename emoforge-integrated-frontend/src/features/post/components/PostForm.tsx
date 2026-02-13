import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Editor } from "@toast-ui/react-editor";
import { API } from "@/shared/api/endpoints";
import { http } from "@/shared/api/httpClient";
import CategorySelect from "./CategorySelect";
import PostTitleInput from "./PostTitleInput";
import PostContentEditor from "./PostContentEditor";
import PostTagInput from "./PostTagInput";
import AttachmentUploader from "./AttachmentUploader";
import FormActions from "./FormActions";
import { useToast } from "@/shared/stores/useToast";
import type { PostDetailDTO } from "@/features/post/types/Post";
import { fixContentForSave } from "@/shared/utils/contentUrlHelper";
import type { PostRequest } from "@/features/post/types/PostRequest";
import { v4 as uuidv4 } from "uuid";
import type { AttachmentItem } from "@/features/post/types/Attachment";

interface PostFormProps {
  mode: "write" | "edit";
  initialData?: PostDetailDTO;
  groupTempKey?: string;
}

export default function PostForm({
  mode,
  initialData,
  groupTempKey,
}: PostFormProps) {
  const editorRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const toast = useToast();

  const attachmentGroupTempKeyRef = useRef(groupTempKey ?? uuidv4());
  const existingPostId = initialData?.id;

  const [formData, setFormData] = useState<PostRequest>({
    title: initialData?.title ?? "",
    categoryId: initialData?.categoryId ?? 0,
    content: initialData?.content ?? "",
    tags: [],
    deleteTagIds: [],
    attachmentIds: [],
  });

  // 신규+기존 첨부파일 관리
  const [attachments, setAttachments] = useState<AttachmentItem[]>([]);
  const [deleteAttachmentIds, setDeleteAttachmentIds] = useState<number[]>([]);

  // 수정 모드일 경우 기존 첨부 로드
  useEffect(() => {
    if (mode === "edit" && existingPostId) {
      http
        .get(`${API.ATTACH}/post/${existingPostId}`, {
          params: { uploadType: "ATTACHMENT" },
        })
        .then((res) => {
          setAttachments(res.data.map((a: any) => ({ ...a, isNew: false })));
        });
    }
  }, [mode, existingPostId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const editorInstance = editorRef.current?.getInstance();
    if (!editorInstance) return;

    let content = editorInstance.getMarkdown();
    content = fixContentForSave(content);

    const basePayload = {
      title: formData.title,
      categoryId: formData.categoryId,
      content,
      ...(formData.tags.length > 0 && {
        tags: formData.tags.map((t) => t.name).join(","),
      }),
    };

    const payload =
      mode === "edit"
        ? {
            ...basePayload,
            id: existingPostId,
            authorUuid: initialData?.memberUuid,
            deleteTagIds: formData.deleteTagIds?.length
              ? formData.deleteTagIds.join(",")
              : undefined,
          }
        : basePayload;

    try {
      let res;
      if (mode === "write") {
        res = await http.post(`${API.POST}`, payload);
      } else {
        res = await http.put(`${API.POST}/${existingPostId}`, payload);
      }
      const postId = res.data;

      if (!postId) throw new Error("postId 없음");

      // 1) 신규 첨부 확정
      if (attachmentGroupTempKeyRef.current) {
        await http.post(`${API.ATTACH}/confirm`, {
          groupTempKey: attachmentGroupTempKeyRef.current,
          postId,
        });
      }

      // 2) 기존 첨부 일괄 삭제
      if (deleteAttachmentIds.length > 0) {
        await http.post(`${API.ATTACH}/delete/batch`, {
          attachmentIds: deleteAttachmentIds,
        });
      }

      // 3) 에디터 이미지 정리
      const mdImgRegex = /!\[[^\]]*]\((?<url>[^)\s]+)(?:\s+"[^"]*")?\)/g;
      const fileUrls: string[] = [];
      for (const match of content.matchAll(mdImgRegex)) {
        const url =
          (match as RegExpMatchArray).groups?.url ??
          (match as RegExpMatchArray)[1];
        if (url && !url.startsWith("blob:")) fileUrls.push(url);
      }
      await http.post(`${API.ATTACH}/cleanup/editor`, {
        postId,
        fileUrls,
      });

      if (mode === "write") toast.success("게시글이 등록되었습니다.");
      if (mode === "edit") toast.success("게시글이 수정되었습니다.");

      navigate(`/${postId}`);
    } catch (error) {
      console.error("게시글 저장 실패", error);
      if (mode === "write") toast.error("게시글 등록 실패");
      if (mode === "edit") toast.error("게시글 수정 실패");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <CategorySelect
        value={formData.categoryId}
        onChange={(val) =>
          setFormData((prev) => ({ ...prev, categoryId: val }))
        }
      />
      <PostTitleInput
        value={formData.title}
        onChange={(val) => setFormData((prev) => ({ ...prev, title: val }))}
      />
      <PostContentEditor
        ref={editorRef}
        value={formData.content}
        onChange={(val) => setFormData((prev) => ({ ...prev, content: val }))}
        groupTempKey={attachmentGroupTempKeyRef.current}
        postId={mode === "edit" ? existingPostId : undefined}
      />
      <PostTagInput
        postId={mode === "edit" ? existingPostId : undefined}
        value={formData.tags}
        onChange={(tags, deleteTagIds) =>
          setFormData((prev) => ({ ...prev, tags, deleteTagIds }))
        }
      />
      <AttachmentUploader
        groupTempKey={attachmentGroupTempKeyRef.current}
        items={attachments}
        setItems={setAttachments}
        deleteIds={deleteAttachmentIds}
        setDeleteIds={setDeleteAttachmentIds}
      />
      <FormActions />
    </form>
  );
}

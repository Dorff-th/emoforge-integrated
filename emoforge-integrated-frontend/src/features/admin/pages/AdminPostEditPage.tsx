import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchPostDetail } from "@/features/post/api/postApi";
import PostForm from "@/features/post/components/PostForm";
import { FileEdit } from "lucide-react";

export default function AdminPostEditPage() {
  const { id } = useParams<{ id: string }>();

  const postId = Number(id);

  /**
   * 1️⃣ 상세 조회
   */
  const {
    data: post,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["post", postId],
    queryFn: () => fetchPostDetail(postId),
    enabled: !!postId,
  });

  if (isLoading) {
    return <div className="max-w-4xl mx-auto py-8 px-4">로딩중...</div>;
  }

  if (isError || !post) {
    return (
      <div className="max-w-4xl mx-auto py-8 px-4">
        게시글을 불러올 수 없습니다.
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <h1 className="flex items-center gap-2 text-xl font-semibold mb-6">
        <FileEdit size={20} />
        Post Edit
      </h1>
      {post && (
        <PostForm
          mode="edit"
          initialData={post}
          apiBase="/api/posts/admin/posts"
          redirectBase="/admin/posts"
        />
      )}
    </div>
  );
}

import { useNavigate } from "react-router-dom";
import { ArrowLeft, ArrowRight } from "lucide-react";

interface NavigationPost {
  id: number;
  title: string;
}

interface PostNavigationProps {
  previousPost: NavigationPost | null;
  nextPost: NavigationPost | null;
  categoryName: string;
}

export default function PostNavigation({
  previousPost,
  nextPost,
  categoryName,
}: PostNavigationProps) {
  const navigate = useNavigate();

  return (
    <section className="mx-auto mt-8 max-w-3xl border-t border-gray-300/70 py-4">
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-lg font-semibold text-stone-900">
          More in {categoryName}
        </h2>
        <span className="text-sm text-stone-500">Previous / Next</span>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <button
          type="button"
          disabled={!previousPost}
          onClick={() => previousPost && navigate(`/posts/${previousPost.id}`)}
          className="flex min-h-20 flex-col items-start justify-between rounded-xl border border-gray-300 bg-white p-4 text-left transition hover:border-gray-400 hover:shadow-sm disabled:cursor-not-allowed disabled:opacity-50"
        >
          <span className="inline-flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.18em] text-stone-500">
            <ArrowLeft size={14} />
            Previous Post
          </span>
          <span className="line-clamp-2 text-base font-semibold text-stone-900">
            {previousPost?.title ?? "이전 게시물이 없습니다."}
          </span>
        </button>

        <button
          type="button"
          disabled={!nextPost}
          onClick={() => nextPost && navigate(`/posts/${nextPost.id}`)}
          className="flex min-h-20 flex-col items-end justify-between rounded-xl border border-gray-300 bg-white p-4 text-right transition hover:border-gray-400 hover:shadow-sm disabled:cursor-not-allowed disabled:opacity-50"
        >
          <span className="inline-flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.18em] text-stone-500">
            Next Post
            <ArrowRight size={14} />
          </span>
          <span className="line-clamp-2 text-base font-semibold text-stone-900">
            {nextPost?.title ?? "다음 게시물이 없습니다."}
          </span>
        </button>
      </div>
    </section>
  );
}

import { useState, useEffect } from "react";
import {
  fetchMemberAttachmentStats,
  fetchMemberPostStats,
  fetchMemberDiaryStats,
} from "@/features/user/api/userStatApi";
import {
  Image,
  Paperclip,
  FileText,
  MessageCircle,
  BookOpen,
  Sparkles,
  Music,
  BarChart3,
} from "lucide-react";

export default function ProfileStatsSection() {
  const [_loading, setLoading] = useState(true);
  const [_error, setError] = useState<string | null>(null);

  const [stats, setStats] = useState<{
    attach?: any;
    posts?: any;
    diary?: any;
  }>({});

  //사용자 통계 불러오기
  const loadAllMemberStats = async () => {
    const [attach, posts, diary] = await Promise.all([
      fetchMemberAttachmentStats(),
      fetchMemberPostStats(),
      fetchMemberDiaryStats(),
    ]);

    return { attach, posts, diary };
  };

  // 📌 ProfilePage 최초 로드 시 통계 조회

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await loadAllMemberStats();
        setStats(data);
      } catch (err: any) {
        console.error("🔴 통계 조회 실패:", err);
        setError("통계를 불러오지 못했어요 🥲");
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  return (
    <section className="space-y-3">
      <div className="my-2 border-t border-gray-200/60" />

      <h3 className="mb-3 flex items-center justify-center gap- text-sm font-semibold text-gray-700">
        <BarChart3 size={16} className="text-gray-500" />
        <span>Profile Statistics</span>
      </h3>
      <div className="grid gap-4">
        <div className="mt-4 grid grid-cols-2 gap-2">
          <StatCard
            label="Images"
            value={stats.attach?.editorImageCount}
            icon={<Image />}
            title="Images uploaded through the editor"
          />
          <StatCard
            label="Files"
            value={stats.attach?.attachmentCount}
            icon={<Paperclip />}
            title="Total files attached to your posts"
          />
          <StatCard
            label="Posts"
            value={stats.posts?.postCount}
            icon={<FileText />}
            title="Posts you have created"
          />
          <StatCard
            label="Comments"
            value={stats.posts?.commentCount}
            icon={<MessageCircle />}
            title="Comments you have written"
          />
        </div>

        <div className="my-2 border-t border-gray-200/60" />

        <div className="grid gap-4">
          <div className="grid grid-cols-2 sm:grid-cols-2 gap-4">
            <StatCard
              label="Records"
              value={stats.diary?.diaryEntryCount}
              icon={<BookOpen />}
              title="Your emotion and reflection records"
            />
            <StatCard
              label="GPT Summary"
              value={stats.diary?.gptSummaryCount}
              icon={<Sparkles />}
              title="AI-generated summaries of your records"
            />
            <StatCard
              label="Music Recs"
              value={stats.diary?.musicRecommendHistoryCount}
              icon={<Music />}
              title="Music recommendations based on your emotions"
            />
          </div>
        </div>
      </div>
    </section>
  );
}

/* ───────── 통계 카드 ───────── */
type StatCardSize = "md" | "sm";
function StatCard({
  size = "md",
  icon,
  label,
  value,
  title,
}: {
  size?: StatCardSize;
  icon: React.ReactNode;
  label: string;
  value: number;
  title?: string;
}) {
  const isSmall = size === "sm";

  return (
    <div
      title={title}
      className={`
        flex flex-col items-center
        rounded-xl bg-gray-50 border border-gray-200
        transition
        hover:bg-gray-100
        ${isSmall ? "p-3" : "p-4"}
      `}
    >
      <div className={isSmall ? "text-gray-500" : "text-gray-600"}>{icon}</div>

      <div
        className={`
          mt-2 font-semibold
          ${isSmall ? "text-lg" : "text-xl"}
        `}
      >
        {value}
      </div>

      <div
        className={`
          text-gray-500
          ${isSmall ? "text-xs" : "text-sm"}
        `}
      >
        {label}
      </div>
    </div>
  );
}

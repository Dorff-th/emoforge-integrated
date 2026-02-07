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
            value={22}
            icon={<Image />}
            title="Images uploaded through the editor"
          />
          <StatCard
            label="Files"
            value={6}
            icon={<Paperclip />}
            title="Total files attached to your posts"
          />
          <StatCard
            label="Posts"
            value={5}
            icon={<FileText />}
            title="Posts you have created"
          />
          <StatCard
            label="Comments"
            value={5}
            icon={<MessageCircle />}
            title="Comments you have written"
          />
        </div>

        <div className="my-2 border-t border-gray-200/60" />

        <div className="grid gap-4">
          <div className="grid grid-cols-2 sm:grid-cols-2 gap-4">
            <StatCard
              label="Records"
              value={22}
              icon={<BookOpen />}
              title="Your emotion and reflection records"
            />
            <StatCard
              label="GPT Summary"
              value={6}
              icon={<Sparkles />}
              title="AI-generated summaries of your records"
            />
            <StatCard
              label="Music Recs"
              value={5}
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

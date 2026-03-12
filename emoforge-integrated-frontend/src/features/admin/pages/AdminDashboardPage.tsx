import { Users, FileText, MessageSquare, Image, Paperclip } from "lucide-react";
import { useDashboard } from "@/features/admin/hooks/useDashboard";

type StatCardProps = {
  title: string;
  value: number;
  icon: React.ReactNode;
  isLoading?: boolean;
};

function StatCard({ title, value, icon, isLoading = false }: StatCardProps) {
  return (
    <div className="bg-white border rounded-xl p-5 shadow-sm flex items-center justify-between">
      <div>
        <p className="text-sm text-gray-500">{title}</p>
        <p className="text-2xl font-bold mt-1">
          {isLoading ? "Loading..." : value}
        </p>
      </div>
      <div className="text-gray-500">{icon}</div>
    </div>
  );
}

export default function AdminDashboardPage() {
  const { data: stats, isLoading } = useDashboard();

  return (
    <div className="p-6">
      <h2 className="text-lg font-bold mb-6">관리자 대시보드</h2>

      {isLoading && (
        <p className="mb-4 text-sm text-gray-500">
          대시보드 통계를 불러오는 중입니다.
        </p>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
        <StatCard
          title="회원 수"
          value={stats?.memberCount ?? 0}
          icon={<Users size={22} />}
          isLoading={isLoading}
        />
        <StatCard
          title="게시글 수"
          value={stats?.postCount ?? 0}
          icon={<FileText size={22} />}
          isLoading={isLoading}
        />
        <StatCard
          title="댓글 수"
          value={stats?.commentCount ?? 0}
          icon={<MessageSquare size={22} />}
          isLoading={isLoading}
        />
        <StatCard
          title="프로필 이미지"
          value={stats?.profileImageCount ?? 0}
          icon={<Image size={22} />}
          isLoading={isLoading}
        />
        <StatCard
          title="에디터 이미지"
          value={stats?.editorImageCount ?? 0}
          icon={<Image size={22} />}
          isLoading={isLoading}
        />
        <StatCard
          title="일반 첨부파일"
          value={stats?.attachmentCount ?? 0}
          icon={<Paperclip size={22} />}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
}

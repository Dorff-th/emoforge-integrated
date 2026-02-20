import { Link } from "react-router-dom";
import { fetchMemberPostStatsToday } from "@/features/user/api/userStatApi";
import { useState, useEffect } from "react";

const TodayPostActivitySection = () => {
  const [_loading, setLoading] = useState(true);
  const [_error, setError] = useState<string | null>(null);

  const [stats, setStats] = useState<{
    posts?: any;
  }>({});

  const loadTodayPostStats = async () => {
    const posts = await fetchMemberPostStatsToday();
    return { posts };
  };

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await loadTodayPostStats();
        setStats(data);
      } catch (err: any) {
        console.error("🔴 오늘의 게시판 통계 조회 실패:", err);
        setError("오늘의 게시판 통계를 불러오지 못했어요 🥲");
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  return (
    <section className="mb-8 rounded-xl border bg-background">
      <div className="px-4 py-3 activity-card">
        <h3 className="text-sm font-semibold mb-3">오늘의 활동</h3>

        <div className="flex justify-around text-center mb-4">
          <div>
            <div className="text-2xl font-bold">{stats.posts?.postCount}</div>
            <div className="text-xs opacity-70">게시글</div>
          </div>
          <div>
            <div className="text-2xl font-bold">
              {stats.posts?.commentCount}
            </div>
            <div className="text-xs opacity-70">댓글</div>
          </div>
        </div>

        <Link
          to="/user/posts/new"
          className="block text-center text-sm font-medium underline opacity-80 hover:opacity-100"
        >
          ✨ 새 글 작성하기
        </Link>
      </div>
    </section>
  );
};

export default TodayPostActivitySection;

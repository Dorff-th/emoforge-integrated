import { Link } from "react-router-dom";

export default function RecentDiaryPreview() {
  return (
    <section className="space-y-4">
      <h2 className="text-lg font-semibold">최근 회고</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {/* TODO: recentDiaries.map */}
        <div className="rounded-xl border p-4 space-y-1">
          <p className="text-sm font-medium">2026-02-03</p>
          <p className="text-sm text-gray-600 line-clamp-2">
            이 날의 회고 요약이 들어갑니다.
          </p>
        </div>

        <div className="rounded-xl border p-4 space-y-1">
          <p className="text-sm font-medium">2026-02-02</p>
          <p className="text-sm text-gray-600 line-clamp-2">
            이 날의 회고 요약이 들어갑니다.
          </p>
        </div>
      </div>

      <div>
        <button className="text-sm underline">
          <Link to="/user/diary/calendar">전체 회고 보기</Link>
        </button>
        {/* /user/diary/calendar  or /user/calendar/list로 이동 */}
      </div>
    </section>
  );
}

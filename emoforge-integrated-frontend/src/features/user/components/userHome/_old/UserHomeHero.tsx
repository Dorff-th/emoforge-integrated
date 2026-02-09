import { Link } from "react-router-dom";

export default function UserHomeHero() {
  return (
    <section className="space-y-2">
      <h1 className="text-2xl font-semibold">오늘의 기록을 시작해볼까요?</h1>

      <p className="text-sm text-gray-500">
        2026-02-05 · 오늘의 회고가 아직 없습니다
      </p>

      <div className="pt-4">
        <button className="px-4 py-2 rounded-lg bg-black text-white text-sm">
          <Link to="/user/diary/write">오늘의 회고 쓰기</Link>
        </button>
      </div>
    </section>
  );
}

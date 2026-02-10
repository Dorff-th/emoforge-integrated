import { Link } from "react-router-dom";
const TodayPostActivitySection = () => {
  return (
    <section className="mb-8 rounded-xl border bg-background">
      <div className="px-4 py-3 activity-card">
        <h3 className="text-sm font-semibold mb-3">오늘의 활동</h3>

        <div className="flex justify-around text-center mb-4">
          <div>
            <div className="text-2xl font-bold">0</div>
            <div className="text-xs opacity-70">게시글</div>
          </div>
          <div>
            <div className="text-2xl font-bold">0</div>
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

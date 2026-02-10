import { Link } from "react-router-dom";

export default function DiaryQucikAction() {
  return (
    <div className="grid grid-cols-2 gap-4 mt-6">
      <Link to="/user/diary/write" className="action-card">
        ✍️ 오늘 기록하기
      </Link>
      <Link to="/user/diary/calendar" className="action-card">
        🗓️ 기록 한눈에 보기
      </Link>
    </div>
  );
}

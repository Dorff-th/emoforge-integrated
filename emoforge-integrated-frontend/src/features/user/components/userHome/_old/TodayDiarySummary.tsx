import { Link } from "react-router-dom";
import EmotionSummaryCard from "./todayDiaryCards/EmotionSummaryCard";
import HabitSummaryCard from "./todayDiaryCards/HabitSummaryCard";
import DiaryPreviewCard from "./todayDiaryCards/DiaryPreviewCard";
import GptFeedbackPreviewCard from "./todayDiaryCards/GptFeedbackPreviewCard";

interface TodayDiarySummaryProps {
  hasTodayDiary: boolean;
}

export default function TodayDiarySummary({
  hasTodayDiary,
}: TodayDiarySummaryProps) {
  //test
  //hasTodayDiary = true;

  return (
    <section className="space-y-4">
      <h2 className="text-lg font-semibold">오늘의 상태</h2>

      {hasTodayDiary ? (
        <div className="space-y-3">
          <EmotionSummaryCard />
          <HabitSummaryCard />
          <DiaryPreviewCard />
          <GptFeedbackPreviewCard />
        </div>
      ) : (
        <div className="rounded-xl border border-dashed p-6 text-center space-y-3">
          <p className="text-sm text-gray-500">아직 오늘의 기록이 없어요</p>
          <button className="text-sm underline">
            <Link to="/user/diary/write">지금 기록하기</Link>
          </button>
        </div>
      )}
    </section>
  );
}

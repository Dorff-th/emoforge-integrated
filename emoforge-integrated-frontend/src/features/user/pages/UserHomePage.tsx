import { useNavigate } from "react-router-dom";
import { useUILoading } from "@/shared/stores/useUILoading";
import { SectionLoading } from "@/shared/components/SectionLoading";
import { TodayHeroSection } from "@/features/user/components/userHome/TodayHeroSection";
import { TodaySummarySection } from "@/features/user/components/userHome/TodaySummarySection";
import { TodayDiarySection } from "@/features/user/components/userHome/TodayDiarySection";
import { useTodayHomeData } from "@/features/user/hooks/useTodayHomeData";
import DiaryQucikAction from "../components/userHome/DiaryQuickActions";
import TodayPostActivitySection from "../components/userHome/TodayPostActivitySection";

export default function UserHomePage() {
  useUILoading("user:home", { duration: 150 }); // SectionLoading Test

  //const todayData = useTodayHomeData(); // 나중에

  const navigate = useNavigate();

  const { entries, averageEmotion, summary, hasTodayDiary } =
    useTodayHomeData();

  return (
    <SectionLoading scope="user:home">
      <div className="w-full max-w-4xl rounded-xl mx-auto bg-white p-2 shadow-lg">
        <div className="min-h-screen bg-[var(--bg)] text-[var(--text)] transition-colors">
          <div className="max-w-2xl mx-auto px-4 py-8 space-y-8">
            <h2 className="text-2xl font-bold mb-4">📅 Today My History</h2>
            <TodayHeroSection
              hasTodayDiary={hasTodayDiary}
              averageEmotion={averageEmotion}
              onWriteToday={() => navigate("/user/diary/write")}
            />
            <TodaySummarySection summary={summary} />
            <TodayDiarySection entries={entries} maxPreview={2} />
            <DiaryQucikAction />
            <TodayPostActivitySection />
          </div>
        </div>
      </div>
    </SectionLoading>
  );
}

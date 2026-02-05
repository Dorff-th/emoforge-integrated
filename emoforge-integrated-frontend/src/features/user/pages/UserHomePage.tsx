import { useUILoading } from "@/shared/stores/useUILoading";
import { SectionLoading } from "@/shared/components/SectionLoading";
import UserHomeHero from "@/features/user/components/userHome/UserHomeHero";
import TodayDiarySummary from "@/features/user/components/userHome/TodayDiarySummary";
import RecentDiaryPreview from "@/features/user/components/userHome/RecentDiaryPreview";

export default function UserHomePage() {
  useUILoading("user:home", { duration: 300 }); // SectionLoading Test

  const hasTodayDiary = false;

  return (
    <SectionLoading scope="user:home">
      <UserHomeHero />
      <TodayDiarySummary hasTodayDiary={hasTodayDiary} />
      <RecentDiaryPreview />
    </SectionLoading>
  );
}

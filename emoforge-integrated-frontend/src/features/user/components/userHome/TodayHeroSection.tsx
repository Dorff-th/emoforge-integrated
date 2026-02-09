// user/components/userHome/TodayHeroSection.tsx

import { NoTodayDiaryState } from "./NoTodayDiaryState";
import { TodayEmotionState } from "./TodayEmotionState";

export interface TodayHeroSectionProps {
  hasTodayDiary: boolean;
  averageEmotion?: number;
  onWriteToday: () => void;
}

export function TodayHeroSection({
  hasTodayDiary,
  averageEmotion,
  onWriteToday,
}: TodayHeroSectionProps) {
  return (
    <section className="mb-8 rounded-xl border bg-background p-6">
      {hasTodayDiary ? (
        <TodayEmotionState averageEmotion={averageEmotion} />
      ) : (
        <NoTodayDiaryState onWriteToday={onWriteToday} />
      )}
    </section>
  );
}

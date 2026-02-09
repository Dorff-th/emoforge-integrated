import { EmotionEmojiDisplay } from "@/features/user/components/userHome/EmotionEmojiDisplay";
import { toEmotionLevel } from "@/shared/utils/emotion";

export function TodayEmotionState({
  averageEmotion,
}: {
  averageEmotion?: number;
}) {
  return (
    <div className="flex items-center gap-4">
      <EmotionEmojiDisplay emotion={toEmotionLevel(averageEmotion)} size="lg" />

      <div className="flex flex-col gap-1">
        <span className="text-sm text-muted-foreground">오늘의 감정</span>

        <span className="text-lg font-medium">
          오늘 하루의 분위기가 기록되어 있어요
        </span>
      </div>
    </div>
  );
}

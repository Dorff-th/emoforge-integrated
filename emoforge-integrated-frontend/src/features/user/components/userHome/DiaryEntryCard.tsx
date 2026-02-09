import { type DiaryEntry } from "@/shared/types/diary";
import { EmotionEmojiDisplay } from "@/features/user/components/userHome/EmotionEmojiDisplay";

export function DiaryEntryCard({
  entry,
  variant = "default",
}: {
  entry: DiaryEntry;
  variant?: "compact" | "default";
}) {
  const parsedHabits = (habitTags: string) => {
    try {
      return JSON.parse(habitTags);
    } catch {
      return [];
    }
  };

  return (
    <div
      className={`rounded-lg border bg-background p-4 ${
        variant === "compact" ? "text-sm" : ""
      }`}
    >
      {/* Header */}
      <div className="mb-2 flex items-center gap-2">
        <EmotionEmojiDisplay emotion={entry.emotion} size="sm" />
        <span className="text-xs text-muted-foreground">{entry.date}</span>
      </div>

      {/* Feeling */}
      {(entry.feelingKo || entry.feelingEn) && (
        <div className="mb-2 text-xs text-muted-foreground">
          {entry.feelingKo}
          {entry.feelingEn && ` / ${entry.feelingEn}`}
        </div>
      )}

      {/* Content */}
      <p className="whitespace-pre-line">{entry.content}</p>

      {/* Habit Tags */}
      {/* {entry.habitTags && (
        <div className="mt-2 flex flex-wrap gap-1">
          {entry.habitTags.split(",").map((tag) => (
            <span key={tag} className="rounded bg-muted px-2 py-0.5 text-xs">
              #{tag.trim()}
            </span>
          ))}
        </div>
      )} */}
      <div className="mt-2 flex flex-wrap gap-1">
        {parsedHabits(entry.habitTags).join(",") || "없음"}
      </div>

      {/* GPT Feedback (Calendar에서만 풀로) */}
      {variant === "default" && entry.feedback && (
        <div className="mt-3 rounded bg-muted p-2 text-xs">
          {entry.feedback}
        </div>
      )}
    </div>
  );
}

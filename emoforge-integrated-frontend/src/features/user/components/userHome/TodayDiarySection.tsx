import { useState } from "react";
import { type DiaryEntry } from "@/shared/types/diary";
import { DiaryEntryCard } from "@/features/user/components/userHome/DiaryEntryCard";

interface TodayDiarySectionProps {
  entries: DiaryEntry[];
  maxPreview?: number;
}

export function TodayDiarySection({
  entries,
  maxPreview = 1,
}: TodayDiarySectionProps) {
  const [expanded, setExpanded] = useState(false);

  const hasEntries = entries.length > 0;
  const visibleEntries = expanded ? entries : entries.slice(0, maxPreview);

  return (
    <section className="rounded-xl border bg-background p-5">
      <header className="mb-4 flex items-center justify-between">
        <h3 className="text-sm font-semibold text-muted-foreground">
          📓 오늘의 기록
        </h3>

        {entries.length > maxPreview && (
          <button
            onClick={() => setExpanded((v) => !v)}
            className="text-xs text-primary hover:underline"
          >
            {expanded ? "접기" : `+ ${entries.length - maxPreview}개 더 보기`}
          </button>
        )}
      </header>

      {!hasEntries ? (
        <EmptyDiaryState />
      ) : (
        <DiaryEntryList entries={visibleEntries} />
      )}
    </section>
  );
}

function EmptyDiaryState() {
  return (
    <p className="text-sm text-muted-foreground">
      아직 오늘 작성된 기록이 없어요.
    </p>
  );
}

function DiaryEntryList({ entries }: { entries: DiaryEntry[] }) {
  return (
    <div className="space-y-4">
      {entries.map((entry) => (
        <DiaryEntryCard entry={entry} variant="default" />
      ))}
    </div>
  );
}

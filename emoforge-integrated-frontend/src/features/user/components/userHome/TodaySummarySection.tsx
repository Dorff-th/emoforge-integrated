// user/components/userHome/TodaySummarySection.tsx

interface TodaySummarySectionProps {
  summary?: string; // GPT 요약 (없을 수 있음)
  dateLabel?: string; // "2026-02-09" / "오늘" 등
  onOpenDetail?: () => void;
}

export function TodaySummarySection({
  summary,
  dateLabel = "오늘",
  onOpenDetail,
}: TodaySummarySectionProps) {
  return (
    <section className="rounded-xl border bg-background p-5">
      <header className="mb-3 flex items-center justify-between">
        <h3 className="text-sm font-semibold text-muted-foreground">
          🧠 {dateLabel}의 요약
        </h3>

        {onOpenDetail && (
          <button
            onClick={onOpenDetail}
            className="text-xs text-primary hover:underline"
          >
            자세히 보기
          </button>
        )}
      </header>

      {summary ? <SummaryContent text={summary} /> : <EmptySummary />}
    </section>
  );
}

function SummaryContent({ text }: { text: string }) {
  return (
    <p className="whitespace-pre-line text-sm leading-relaxed text-foreground">
      {text}
    </p>
  );
}

function EmptySummary() {
  return (
    <p className="text-sm text-muted-foreground">
      아직 오늘의 요약이 생성되지 않았어요.
    </p>
  );
}

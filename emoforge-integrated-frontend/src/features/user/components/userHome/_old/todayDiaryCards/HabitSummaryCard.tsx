export default function HabitSummaryCard() {
  return (
    <div className="rounded-xl p-4 bg-blue-50 space-y-2">
      <p className="text-sm font-medium">오늘 실천한 습관</p>

      <div className="flex flex-wrap gap-2">
        <span className="px-2 py-1 text-xs rounded-full bg-white border">
          일찍 일어나기
        </span>
      </div>
    </div>
  );
}

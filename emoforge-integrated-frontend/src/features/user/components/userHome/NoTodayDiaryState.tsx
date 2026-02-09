import { Button } from "@headlessui/react";

export function NoTodayDiaryState({
  onWriteToday,
}: {
  onWriteToday: () => void;
}) {
  return (
    <div className="flex flex-col gap-4">
      <h2 className="text-xl font-semibold">오늘의 기록을 시작해볼까요?</h2>

      <p className="text-sm text-muted-foreground">
        아직 오늘의 감정과 회고가 입력되지 않았어요.
      </p>

      <div>
        <Button onClick={onWriteToday}>오늘의 회고 쓰기</Button>
      </div>
    </div>
  );
}

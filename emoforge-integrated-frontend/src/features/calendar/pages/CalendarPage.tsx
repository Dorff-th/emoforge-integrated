import Calendar from "../components/Calendar";

export default function CalendarPage() {
  return (
    <div className="w-full max-w-[60rem] rounded-xl mx-auto bg-white p-2 shadow-lg">
      <div className="bg-white dark:bg-gray-700 text-black dark:text-white rounded-lg shadow-lg p-8 min-h-[650px]">
        <h2 className="text-2xl font-bold mb-4">📅 캘린더 페이지</h2>
        <p className="text-sm text-gray-600 dark:text-gray-300 mb-6">
          감정 & 회고 입력 여부를 달력에서 한눈에 확인해보세요.
        </p>
        <Calendar />
      </div>
    </div>
  );
}

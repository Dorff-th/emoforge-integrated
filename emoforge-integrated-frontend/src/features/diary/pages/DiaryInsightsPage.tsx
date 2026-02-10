import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import React, { useEffect, useState } from "react";
import { fetchEmotionStatistics } from "@/features/statistics/api/statisticsApi";
import EmotionBarChart from "@/features/statistics/components/EmotionBarChart";
import WeeklyLineChart from "@/features/statistics/components/WeeklyLineChart";
import DayOfWeekBarChart from "@/features/statistics/components/DayOfWeekBarChart";
import { getDateRange } from "@/shared/utils/dateUtils";
import { type PeriodOption } from "@/features/statistics/types/statisticsTypes";
import EmotionSummaryCard from "@/features/statistics/components/EmotionSummaryCard";

export default function DiaryInsightsPage() {
  const [period, setPeriod] = useState<PeriodOption>("this-month");
  const [[startDateStr, endDateStr], setDateRange] = useState<[string, string]>(
    getDateRange("this-month"),
  );

  const [customStartDate, setCustomStartDate] = useState<Date | null>(null);
  const [customEndDate, setCustomEndDate] = useState<Date | null>(null);

  const [data, setData] = useState<any>(null);

  useEffect(() => {
    if (startDateStr && endDateStr) {
      fetchEmotionStatistics(startDateStr, endDateStr)
        .then(setData)
        .catch((err) => console.error("통계 불러오기 실패", err));
    }
  }, [startDateStr, endDateStr]);

  const handlePeriodChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value as PeriodOption;
    setPeriod(value);
    if (value !== "custom") {
      setDateRange(getDateRange(value));
    }
  };

  // custom 범위 설정 시 문자열로 변환해서 적용
  const handleCustomDateApply = () => {
    if (customStartDate && customEndDate) {
      const format = (d: Date) => d.toISOString().split("T")[0];
      setDateRange([format(customStartDate), format(customEndDate)]);
    }
  };
  return (
    <div className="w-full max-w-3xl rounded-xl mx-auto bg-white p-2 shadow-lg">
      <div className="min-h-screen rounded-xl bg-gradient-to-b from-blue-100 to-white dark:from-gray-900 dark:to-gray-800 transition-colors duration-500">
        <div className="p-4 space-y-6">
          <h2 className="text-2xl font-bold">감정 통계 대시보드</h2>

          {/* ✅ 기간 선택 드롭다운 */}
          <div className="space-y-2">
            <select
              value={period}
              onChange={handlePeriodChange}
              className="border p-1 rounded"
            >
              <option value="this-week">이번 주</option>
              <option value="this-month">이번 달</option>
              <option value="last-7-days">최근 7일</option>
              <option value="custom">직접 선택</option>
            </select>

            {/* ✅ custom 선택 시 날짜 선택기 노출 */}
            {period === "custom" && (
              <div className="flex items-center gap-2">
                <DatePicker
                  selected={customStartDate}
                  onChange={(date: Date | null) => setCustomStartDate(date)}
                  selectsStart
                  startDate={customStartDate}
                  endDate={customEndDate}
                  placeholderText="시작 날짜"
                  dateFormat="yyyy-MM-dd"
                />
                <span>~</span>
                <DatePicker
                  selected={customEndDate}
                  onChange={(date: Date | null) => setCustomEndDate(date)}
                  selectsEnd
                  startDate={customStartDate}
                  endDate={customEndDate}
                  minDate={customStartDate ?? undefined}
                  placeholderText="종료 날짜"
                  dateFormat="yyyy-MM-dd"
                />
                <button
                  onClick={handleCustomDateApply}
                  className="px-2 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                  적용
                </button>
              </div>
            )}
          </div>

          {/* ✅ 통계 렌더링 */}
          {data && (
            <>
              <div className="text-lg">
                평균 감정 점수:{" "}
                <strong>{data.averageEmotion.toFixed(2)}</strong>
              </div>
              <EmotionSummaryCard average={data.averageEmotion} />
              <EmotionBarChart frequency={data.emotionFrequency} />
              <WeeklyLineChart trend={data.weeklyTrend} />
              <DayOfWeekBarChart dayAverage={data.dayOfWeekAverage} />
            </>
          )}
        </div>
      </div>
    </div>
  );
}

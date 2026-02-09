import { useState, useEffect } from "react";
import { type CalendarDayData } from "../types/calendar.types";
import { getDaysInMonth } from "@/shared/utils/calendarUtils";

export const useCalendarData = (year: number, month: number) => {
  const [data, setData] = useState<CalendarDayData[]>([]);

  useEffect(() => {
    const days = getDaysInMonth(year, month);

    const enrichedData: CalendarDayData[] = days.map((dateStr) => {
      let averageScore: number | undefined = undefined;
      let emoji: string | undefined = undefined;

      return {
        date: dateStr,
        emotionScore: averageScore,
        emotionEmoji: emoji,
      };
    });

    setData(enrichedData);
  }, [year, month]);

  return {
    calendarData: data,
  };
};

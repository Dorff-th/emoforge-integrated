import {
  startOfWeek,
  endOfWeek,
  startOfMonth,
  endOfMonth,
  subDays,
  format,
} from 'date-fns';
import { type PeriodOption } from '@/features/statistics/types/statisticsTypes';

export const getDateRange = (option: PeriodOption): [string, string] => {
  const today = new Date();
  switch (option) {
    case 'this-week':
      return [
        format(startOfWeek(today, { weekStartsOn: 1 }), 'yyyy-MM-dd'),
        format(endOfWeek(today, { weekStartsOn: 1 }), 'yyyy-MM-dd'),
      ];
    case 'this-month':
      return [
        format(startOfMonth(today), 'yyyy-MM-dd'),
        format(endOfMonth(today), 'yyyy-MM-dd'),
      ];
    case 'last-7-days':
      return [
        format(subDays(today, 6), 'yyyy-MM-dd'),
        format(today, 'yyyy-MM-dd'),
      ];
    default:
      return ['', '']; // custom 선택 시 별도 처리
  }
};

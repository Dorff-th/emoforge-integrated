// src/features/calendar/components/CalendarSelector.tsx
import React, { useState } from 'react';

interface CalendarSelectorProps {
  currentDate: Date;
  setCurrentDate: (date: Date) => void;
}

const CalendarSelector: React.FC<CalendarSelectorProps> = ({ currentDate, setCurrentDate }) => {
  const [isOpen, setIsOpen] = useState(false);

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth() + 1;

  const handleYearChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newYear = parseInt(e.target.value, 10);
    setCurrentDate(new Date(newYear, currentDate.getMonth(), 1));
    setIsOpen(false); // 자동 닫기
  };

  const handleMonthChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newMonth = parseInt(e.target.value, 10) - 1;
    setCurrentDate(new Date(currentDate.getFullYear(), newMonth, 1));
    setIsOpen(false); // 자동 닫기
  };

  return (
    <div className="relative">
      <button
  onClick={() => setIsOpen(!isOpen)}
  className="flex items-end gap-2 focus:outline-none"
>
  <span className="text-4xl font-bold text-blue-900">{currentDate.getMonth() + 1}</span>
  <span className="text-base font-medium text-black uppercase tracking-wide">
    {currentDate.toLocaleString('en-US', { month: 'long' })}
  </span>
  <span className="text-base text-gray-500 font-medium">{currentDate.getFullYear()}</span>
</button>

      {isOpen && (
        <div className="absolute top-full mt-2 left-1/2 -translate-x-1/2 bg-white border rounded-md shadow-md z-10 p-2 flex gap-2">
          <select
            value={year}
            onChange={handleYearChange}
            className="border rounded px-2 py-1"
          >
            {Array.from({ length: 10 }, (_, i) => {
              const y = new Date().getFullYear() - 5 + i;
              return (
                <option key={y} value={y}>
                  {y}년
                </option>
              );
            })}
          </select>

          <select
            value={month}
            onChange={handleMonthChange}
            className="border rounded px-2 py-1"
          >
            {Array.from({ length: 12 }, (_, i) => (
              <option key={i + 1} value={i + 1}>
                {i + 1}월
              </option>
            ))}
          </select>
        </div>
      )}
    </div>
  );
};

export default CalendarSelector;

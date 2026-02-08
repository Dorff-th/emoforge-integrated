import React from "react";

interface HabitChecklistProps {
  selectedHabits: string[];
  onChange: (updated: string[]) => void;
}

// 기본 습관 목록 (필요 시 props로 받는 구조도 가능)
const defaultHabits = [
  "운동",
  "명상",
  "일찍 일어나기",
  "물 2L 마시기",
  "디지털 디톡스",
];

const HabitChecklist: React.FC<HabitChecklistProps> = ({
  selectedHabits,
  onChange,
}) => {
  const toggleHabit = (habit: string) => {
    if (selectedHabits.includes(habit)) {
      onChange(selectedHabits.filter((h) => h !== habit));
    } else {
      onChange([...selectedHabits, habit]);
    }
  };

  return (
    <div
      className="bg-gradient-to-b
      from-gray-100 to-gray-50
      dark:from-gray-800 dark:to-gray-700
      text-gray-900 dark:text-gray-100
      border border-gray-200 dark:border-gray-700
      shadow-sm
      transition-colors duration-500
      rounded-2xl
      p-4 mb-6"
    >
      <h3 className="text-lg font-semibold mb-2">오늘 완료한 습관</h3>
      <div className="flex flex-wrap gap-3">
        {defaultHabits.map((habit) => (
          <label key={habit} className="flex items-center gap-2 cursor-pointer">
            <input
              type="checkbox"
              checked={selectedHabits.includes(habit)}
              onChange={() => toggleHabit(habit)}
              className="accent-blue-500"
            />
            <span>{habit}</span>
          </label>
        ))}
      </div>
    </div>
  );
};

export default HabitChecklist;

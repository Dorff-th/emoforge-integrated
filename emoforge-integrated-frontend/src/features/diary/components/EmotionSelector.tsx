import React from "react";
import clsx from "clsx";
import { emotionList } from "@/features/diary/types/emotionMap";

interface EmotionSelectorProps {
  selected: number;
  onChange: (value: number) => void;
}

const EmotionSelector: React.FC<EmotionSelectorProps> = ({
  selected,
  onChange,
}) => {
  return (
    <div
      className="
      bg-gradient-to-b
      from-gray-100 to-gray-50
      dark:from-gray-800 dark:to-gray-700
      text-gray-900 dark:text-gray-100
      border border-gray-200 dark:border-gray-700
      shadow-sm
      transition-colors duration-500
      rounded-2xl
      p-4 mb-6
    "
    >
      <h3 className="text-lg font-semibold mb-2">오늘 감정은 어땠나요?</h3>
      <div className="flex justify-between gap-4">
        {emotionList.map((item) => (
          <button
            key={item.value}
            onClick={() => onChange(item.value)}
            className={clsx(
              "text-3xl transition-transform duration-200",
              selected === item.value
                ? "scale-125 border-2 border-blue-400 rounded-full"
                : "opacity-70",
            )}
            title={item.label}
          >
            {item.emoji}
          </button>
        ))}
      </div>
    </div>
  );
};

export default EmotionSelector;

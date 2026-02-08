import React from "react";
import {
  type FeedbackType,
  feedbackStyleMap,
} from "@/features/gpt/types/feedbackTypes";

interface FeedbackTypeSelectProps {
  value: FeedbackType;
  onChange: (val: FeedbackType) => void;
}

//export type FeedbackType = '위로' | '칭찬' | '갈굼' | '비웃음' | '랜덤';

//const options: FeedbackType[] = ['위로', '칭찬', '갈굼', '비웃음', '랜덤'];

const FeedbackTypeSelect: React.FC<FeedbackTypeSelectProps> = ({
  value,
  onChange,
}) => {
  return (
    <div className="bg-gradient-to-b from-blue-100 to-blue-50 dark:from-gray-800 dark:to-gray-700 shadow-lg transition-colors duration-500 rounded-2xl p-4 mb-6 border border-gray-200 dark:border-gray-700">
      <h3 className="text-lg font-semibold mb-2">GPT 피드백 스타일 선택</h3>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value as FeedbackType)}
        className="border border-gray-300 px-4 py-2 rounded w-full text-black bg-white dark:bg-white"
      >
        {Object.entries(feedbackStyleMap).map(([key, style]) => (
          <option key={key} value={key}>
            {style.emoji} {style.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default FeedbackTypeSelect;

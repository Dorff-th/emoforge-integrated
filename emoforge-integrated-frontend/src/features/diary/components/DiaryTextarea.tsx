import React from "react";

interface DiaryTextareaProps {
  value: string;
  onChange: (val: string) => void;
}

const DiaryTextarea: React.FC<DiaryTextareaProps> = ({ value, onChange }) => {
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
      <h3 className="text-lg font-semibold mb-2">오늘의 회고</h3>
      <textarea
        placeholder="하루를 되돌아보며 자유롭게 적어보세요..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full h-40 border border-gray-300 rounded px-3 py-2 resize-none text-black bg-white dark:bg-white dark:text-black placeholder:text-gray-400"
      />
    </div>
  );
};

export default DiaryTextarea;

// src/components/feedback/GPTFeedbackModal.tsx
import { useEffect } from "react";
import {
  feedbackStyleMap,
  type FeedbackType,
} from "@/features/gpt/types/feedbackTypes";

interface GPTFeedbackModalProps {
  message: string;
  onClose: () => void;
  type?: FeedbackType;
  duration?: number;
}

const GPTFeedbackModal = ({
  message,
  onClose,
  type = "default",
  duration = 3000,
}: GPTFeedbackModalProps) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);
    return () => clearTimeout(timer);
  }, [duration, onClose]);

  const style = feedbackStyleMap[type];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
      <div
        className={`animate-slideDownCenter ${style.bgColor} border-2 ${style.borderColor} text-black dark:text-black rounded-xl shadow-2xl w-[90%] max-w-md px-6 py-6`}
      >
        <p className="text-xs text-gray-600 dark:text-black-300 mb-1">
          저장 중...
        </p>
        <div className="flex items-center gap-3">
          <span className="text-xl">{style.emoji}</span>
          <span className="text-base font-medium">{message}</span>
        </div>
        <div className="mt-4 text-center">
          <button
            onClick={onClose}
            className="px-5 py-1.5 bg-blue-500 text-white rounded-md hover:bg-blue-600 text-sm font-semibold"
          >
            OK
          </button>
        </div>
      </div>
    </div>
  );
};

export default GPTFeedbackModal;

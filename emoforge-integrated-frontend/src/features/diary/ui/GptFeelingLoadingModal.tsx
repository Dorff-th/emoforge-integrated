import React from "react";
import { Dialog } from "@headlessui/react";
import { Sparkles } from "lucide-react"; // 또는 다른 감성적인 아이콘

interface GptFeelingLoadingModalProps {
  isOpen: boolean;
}

const GptFeelingLoadingModal: React.FC<GptFeelingLoadingModalProps> = ({
  isOpen,
}) => {
  if (!isOpen) return null;

  return (
    <Dialog
      open={isOpen}
      onClose={() => {}}
      className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-30"
    >
      <div className="bg-white dark:bg-gray-800 rounded-2xl p-8 shadow-xl text-center max-w-sm mx-auto animate-pulse">
        <div className="flex justify-center mb-4">
          <Sparkles className="w-10 h-10 text-blue-500 animate-spin-slow" />
        </div>
        <h2 className="text-lg font-semibold text-gray-800 dark:text-white mb-2">
          GPT가 당신의 기분을 말로 정리 중이에요...
        </h2>
        <p className="text-sm text-gray-600 dark:text-gray-300">
          감정에 딱 맞는 영어 표현을 고민하고 있어요 💭
        </p>
      </div>
    </Dialog>
  );
};

export default GptFeelingLoadingModal;

import React, { useState } from "react";
import clsx from "clsx";
import { useToast } from "@/shared/stores/useToast";
import { langHttp } from "@/shared/api/langHttpClient";
import { SKIP_LOADING_HEADER } from "@/shared/api/httpClient";
import GptFeelingLoadingModal from "@/features/diary/ui/GptFeelingLoadingModal";

interface FeelingInputProps {
  value: string;
  onChange: (val: string) => void;
  selectedEnglish: string;
  onEnglishSelect: (val: string) => void;
}

const FeelingInput: React.FC<FeelingInputProps> = ({
  value,
  onChange,
  selectedEnglish,
  onEnglishSelect,
}) => {
  const toast = useToast();

  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchGptSuggestions = async (feelingKo: string) => {
    if (!feelingKo.trim()) return;
    setLoading(true);
    try {
      const response = await langHttp.post(
        "/diary/gpt/feeling",
        { feelingKo },
        {
          headers: {
            [SKIP_LOADING_HEADER]: "1", // 전역 loading skip
          },
        },
      );
      setSuggestions(response.data.suggestions || []);
    } catch (error) {
      toast?.error("GPT 추천 실패");
      console.error("GPT 추천 실패:", error);
      setSuggestions([]);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerate = () => {
    fetchGptSuggestions(value);
  };

  return (
    <>
      <GptFeelingLoadingModal isOpen={loading} />
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
        <h3 className="text-lg font-semibold mb-2">
          오늘 기분 한마디 (한글로)
        </h3>
        <div className="flex gap-2 mb-3">
          <input
            type="text"
            placeholder="예: 뭔가 무기력했어..."
            value={value}
            onChange={(e) => onChange(e.target.value)}
            className="w-full border border-gray-300 rounded px-3 py-2 text-black bg-white dark:bg-white dark:text-black placeholder:text-gray-400"
          />
          <button
            onClick={handleGenerate}
            disabled={loading}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            {loading ? "추천 중..." : "GPT 추천"}
          </button>
        </div>
        {suggestions.length > 0 && (
          <div className="flex flex-wrap gap-3 dark:text-black">
            {suggestions.map((s, idx) => (
              <button
                key={idx}
                onClick={() => onEnglishSelect(s)}
                className={clsx(
                  "px-4 py-2 rounded border text-sm transition-all",
                  selectedEnglish === s
                    ? "bg-blue-100 border-blue-400"
                    : "bg-gray-100 border-gray-300 hover:border-gray-400",
                )}
              >
                {s}
              </button>
            ))}
          </div>
        )}
      </div>
    </>
  );
};

export default FeelingInput;

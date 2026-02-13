import { useState } from "react";
import { Type } from "lucide-react";

interface PostTitleInputProps {
  value?: string;
  onChange?: (value: string) => void;
}

const MAX_LENGTH = 100; // 🔹 제목 최대 길이 제한

export default function PostTitleInput({
  value = "",
  onChange,
}: PostTitleInputProps) {
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;

    if (!newValue.trim()) {
      setError("제목을 입력하세요.");
    } else if (newValue.length > MAX_LENGTH) {
      setError(`제목은 최대 ${MAX_LENGTH}자까지 가능합니다.`);
    } else {
      setError(null);
    }

    onChange?.(newValue);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault(); // 🔹 Enter로 인한 form submit 방지
    }
  };

  return (
    <div>
      <label className="flex items-center gap-2">
        <Type size={16} />
        Title
      </label>
      <input
        type="text"
        value={value}
        onChange={handleChange}
        onKeyDown={handleKeyDown} // 🔹 Enter 방지
        placeholder="제목을 입력하세요"
        maxLength={MAX_LENGTH}
        className={`w-full border rounded-md px-3 py-2 focus:ring focus:ring-blue-200 ${
          error ? "border-red-500" : ""
        }`}
      />
      <div className="flex justify-between mt-1 text-sm">
        <span className="text-gray-500">
          {value.length}/{MAX_LENGTH}
        </span>
        {error && <span className="text-red-500">{error}</span>}
      </div>
    </div>
  );
}

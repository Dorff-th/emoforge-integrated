import { useState, useEffect } from "react";
import { fetchSuggest } from "@/features/post/api/tagApi";
import { getPostTags } from "@/features/post/api/postApi"; // 📌 기존 태그 불러오기
import type { Tag } from "@/features/post/types/Tag";
import { Tag as TagIcon } from "lucide-react";

interface PostTagInputProps {
  postId?: number; // 수정 모드일 경우
  value: Tag[];
  onChange: (tags: Tag[], deleteTagIds: number[]) => void;
}

export default function PostTagInput({
  postId,
  value,
  onChange,
}: PostTagInputProps) {
  const [inputValue, setInputValue] = useState("");
  const [suggestions, setSuggestions] = useState<string[]>([]);
  const [deleteTagIds, setDeleteTagIds] = useState<number[]>([]);
  const [loaded, setLoaded] = useState(false);

  // 🔹 수정 모드: 기존 태그 불러오기
  useEffect(() => {
    if (postId && !loaded) {
      getPostTags(postId).then((res: Tag[]) => {
        onChange(res, []); // 초기 세팅
        setLoaded(true); // 한 번만 실행
      });
    }
  }, [postId, loaded, onChange]);

  // 🔹 입력값 변경
  const handleInputChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value;
    setInputValue(val);

    if (val.trim().length >= 2) {
      const result = await fetchSuggest(val);
      setSuggestions(result);
    } else {
      setSuggestions([]);
    }
  };

  // 🔹 태그 추가 (# 제거 후 저장)
  const addTag = (name: string) => {
    const trimmed = name.replace(/^#/, "");
    if (!value.find((t) => t.name === trimmed)) {
      const newTags = [...value, { id: 0, name: trimmed }]; // 신규 태그는 id=0
      onChange(newTags, deleteTagIds);
    }
    setInputValue("");
    setSuggestions([]);
  };

  // 🔹 Enter/Comma 처리
  // onKeyDown은 입력창에서 인라인으로 처리합니다.

  // 🔹 태그 삭제
  const removeTag = (tag: Tag) => {
    const updated = value.filter((t) => t.name !== tag.name);
    let updatedDeleteIds = [...deleteTagIds];

    if (tag.id && tag.id !== 0) {
      updatedDeleteIds.push(tag.id);
      setDeleteTagIds(updatedDeleteIds);
    }

    onChange(updated, updatedDeleteIds);
  };

  return (
    <div className="space-y-2">
      <label className="flex items-center gap-2">
        <TagIcon size={16} />
        Tag
      </label>
      <div className="flex flex-wrap gap-2 border rounded p-2">
        {/* 태그칩 (표시할 때만 # 붙임) */}
        {value.map((tag) => (
          <span
            key={tag.id || tag.name}
            className="flex items-center bg-gray-200 rounded px-2 py-1 text-sm"
          >
            #{tag.name}
            <button
              type="button"
              onClick={() => removeTag(tag)}
              className="ml-1 text-gray-500 hover:text-red-500"
            >
              ×
            </button>
          </span>
        ))}

        {/* 입력창 */}
        <input
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          onKeyDown={(e) => {
            if (e.key === "Enter" || e.key === ",") {
              e.preventDefault();
              const trimmed = inputValue.trim();
              if (trimmed.length === 0) return; // 🚫 빈 태그 방지!
              addTag(inputValue.trim());
            }
          }}
          className="flex-1 outline-none"
          placeholder="태그 입력 (Enter 또는 ,)"
        />
      </div>

      {/* 자동완성 */}
      {suggestions.length > 0 && (
        <ul className="border rounded bg-white shadow mt-1 max-h-40 overflow-y-auto">
          {suggestions.map((s) => (
            <li
              key={s}
              onClick={() => addTag(s)}
              className="px-3 py-1 hover:bg-gray-100 cursor-pointer"
            >
              {s}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

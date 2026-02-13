import { useEffect, useState } from "react";
import { fetchCategories } from "@/features/post/api/categoryApi";
import type { Category } from "@/features/post/types/Category";
import { Folder } from "lucide-react";

interface CategorySelectProps {
  value?: number; // 선택된 카테고리 id
  onChange?: (id: number) => void;
}

export default function CategorySelect({
  value,
  onChange,
}: CategorySelectProps) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadCategories = async () => {
      try {
        const data = await fetchCategories();
        setCategories(data);
        // ✅ 첫 번째 카테고리를 기본 선택으로 설정
        if (data.length > 0 && !value) {
          onChange?.(data[0].id);
        }
      } catch (err) {
        console.error("카테고리 불러오기 실패:", err);
      } finally {
        setLoading(false);
      }
    };

    loadCategories();
  }, []);

  if (loading) {
    return <p className="text-sm text-gray-500">카테고리 불러오는 중...</p>;
  }

  return (
    <div>
      <label className="flex items-center gap-2">
        <Folder size={16} />
        Category
      </label>
      <select
        className="w-full border rounded-md px-3 py-2 focus:ring focus:ring-blue-200"
        value={value ?? ""}
        onChange={(e) => onChange?.(Number(e.target.value))}
      >
        {categories.map((cat) => (
          <option key={cat.id} value={cat.id}>
            {cat.name}
          </option>
        ))}
      </select>
    </div>
  );
}

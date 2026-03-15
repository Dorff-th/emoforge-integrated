import type { Category } from "@/features/post/types/Category";

interface CategoryFilterProps {
  categories: Category[];
  selectedCategoryId: number | null;
  onSelect: (categoryId: number | null) => void;
}

export default function CategoryFilter({
  categories,
  selectedCategoryId,
  onSelect,
}: CategoryFilterProps) {
  return (
    <div className="mb-4">
      <div className="flex flex-wrap gap-2">
        <button
          type="button"
          onClick={() => onSelect(null)}
          className={`rounded-full px-3 py-1.5 text-sm font-medium transition ${
            selectedCategoryId === null
              ? "bg-blue-600 text-white"
              : "bg-gray-100 text-gray-700 hover:bg-gray-200"
          }`}
        >
          All
        </button>

        {categories.map((category) => (
          <button
            key={category.id}
            type="button"
            onClick={() => onSelect(category.id)}
            className={`rounded-full px-3 py-1.5 text-sm font-medium transition ${
              selectedCategoryId === category.id
                ? "bg-blue-600 text-white"
                : "bg-gray-100 text-gray-700 hover:bg-gray-200"
            }`}
          >
            {category.name}
          </button>
        ))}
      </div>
    </div>
  );
}

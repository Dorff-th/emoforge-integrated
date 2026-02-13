// src/components/post/FormActions.tsx
import { useNavigate } from "react-router-dom";
import { X, Check } from "lucide-react";

export default function FormActions() {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(-1); // ✅ 브라우저 히스토리 상 한 단계 뒤로 이동
  };

  return (
    <div className="flex justify-end gap-3">
      <button
        type="button"
        onClick={handleGoBack}
        className="px-4 py-2 border rounded-md hover:bg-gray-100"
      >
        <X size={16} />
        Cancel
      </button>
      <button
        type="submit"
        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
      >
        <Check size={16} />
        Submit
      </button>
    </div>
  );
}

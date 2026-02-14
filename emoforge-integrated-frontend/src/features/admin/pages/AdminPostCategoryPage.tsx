import { useState } from "react";
import {
  useCategories,
  useCreateCategory,
  useUpdateCategory,
  useDeleteCategory,
} from "@/features/admin/hooks/useCategories";
import type { Category } from "@/features/admin/api/admin.type";
import { Button } from "@/components/ui/button";
import ConfirmModal from "@/shared/components/ConfirmModal";

export default function AdminCategoryPage() {
  const [newCategory, setNewCategory] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingName, setEditingName] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<Category | null>(null);

  const { data: categories = [], isLoading } = useCategories();
  const createMutation = useCreateCategory();
  const updateMutation = useUpdateCategory();
  const deleteMutation = useDeleteCategory();

  const handleAdd = () => {
    if (!newCategory.trim()) return;
    createMutation.mutate(newCategory.trim(), {
      onSuccess: () => setNewCategory(""),
    });
  };

  const handleUpdate = (id: number) => {
    if (!editingName.trim()) return;
    updateMutation.mutate(
      { id, name: editingName.trim() },
      {
        onSuccess: () => {
          setEditingId(null);
          setEditingName("");
        },
      },
    );
  };

  const handleConfirm = () => {
    if (!deleteTarget) return;
    deleteMutation.mutate(deleteTarget.id, {
      onSuccess: () => setDeleteTarget(null),
    });
  };

  const handleCancel = () => {
    setDeleteTarget(null);
  };

  if (isLoading) {
    return <div className="p-4">카테고리 목록을 불러오는 중...</div>;
  }

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">카테고리 관리</h2>

      {/* 추가 */}
      <div className="flex mb-4 gap-2">
        <input
          type="text"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
          placeholder="새 카테고리 이름"
          className="border rounded px-2 py-1 flex-1"
        />
        <Button onClick={handleAdd} disabled={createMutation.isPending}>
          {createMutation.isPending ? "추가 중..." : "추가"}
        </Button>
      </div>

      {/* 목록 */}
      <table className="w-full border">
        <thead>
          <tr className="bg-gray-100">
            <th className="border px-2 py-1">ID</th>
            <th className="border px-2 py-1">이름</th>
            <th className="border px-2 py-1">작업</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((cat) => (
            <tr key={cat.id}>
              <td className="border px-2 py-1">{cat.id}</td>
              <td className="border px-2 py-1">
                {editingId === cat.id ? (
                  <input
                    type="text"
                    value={editingName}
                    onChange={(e) => setEditingName(e.target.value)}
                    className="border rounded px-2 py-1 w-full"
                  />
                ) : (
                  cat.name
                )}
              </td>
              <td className="border px-2 py-1 space-x-2">
                {editingId === cat.id ? (
                  <>
                    <Button
                      onClick={() => handleUpdate(cat.id)}
                      disabled={updateMutation.isPending}
                    >
                      {updateMutation.isPending ? "저장 중..." : "저장"}
                    </Button>
                    <Button
                      variant="secondary"
                      onClick={() => setEditingId(null)}
                    >
                      취소
                    </Button>
                  </>
                ) : (
                  <>
                    <Button
                      variant="secondary"
                      onClick={() => {
                        setEditingId(cat.id);
                        setEditingName(cat.name);
                      }}
                    >
                      수정
                    </Button>
                    <Button
                      variant="destructive"
                      onClick={() => setDeleteTarget(cat)}
                    >
                      삭제
                    </Button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <ConfirmModal
        open={!!deleteTarget}
        title="카테고리 삭제"
        description={
          deleteTarget
            ? `삭제 시 "${deleteTarget.name}" 카테고리의 글은 기본 카테고리로 이동합니다. 계속하시겠습니까?`
            : ""
        }
        onConfirm={handleConfirm}
        onCancel={handleCancel}
      />
    </div>
  );
}

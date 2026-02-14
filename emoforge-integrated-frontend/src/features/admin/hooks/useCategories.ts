import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from '@/features/admin/api/adminCategoryApi';
import { useToast } from '@/shared/stores/useToast';

export const categoryKeys = {
  all: ['categories'] as const,
};

export function useCategories() {
  return useQuery({
    queryKey: categoryKeys.all,
    queryFn: fetchCategories,
  });
}

export function useCreateCategory() {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: (name: string) => createCategory(name),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: categoryKeys.all });
      toast.success('카테고리가 생성되었습니다.');
    },
  });
}

export function useUpdateCategory() {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: ({ id, name }: { id: number; name: string }) => updateCategory(id, name),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: categoryKeys.all });
      toast.success('카테고리가 수정되었습니다.');
    },
  });
}

export function useDeleteCategory() {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: (id: number) => deleteCategory(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: categoryKeys.all });
      toast.success('카테고리가 삭제되었습니다.');
    },
  });
}

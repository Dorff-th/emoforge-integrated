import { useQuery } from "@tanstack/react-query";
import { adminApi } from "@/features/admin/api/adminApi";
import { adminAuthKeys } from "@/features/admin/api/admin.type";

export function useAdminMe() {
  return useQuery({
    queryKey: adminAuthKeys.me,
    queryFn: adminApi.adminMe,
    retry: false,
    staleTime: 5 * 60 * 1000, // 5분
  });
}

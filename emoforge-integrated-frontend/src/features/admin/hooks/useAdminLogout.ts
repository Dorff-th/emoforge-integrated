import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { adminApi } from "@/features/admin/api/adminApi";
import { adminAuthKeys } from "@/features/admin/api/admin.type";

export function useAdminLogout() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: adminApi.adminLogout,
    onSuccess: () => {
      // 로그아웃은 "무효화"가 아니라 "삭제"가 더 깔끔
      queryClient.removeQueries({ queryKey: adminAuthKeys.me });
      navigate("/admin/login", { replace: true });
    },
    onError: () => {
      // 서버 호출 실패하더라도 FE에서는 끊어주는 게 UX상 안전
      queryClient.removeQueries({ queryKey: adminAuthKeys.me });
      navigate("/admin/login", { replace: true });
    },
  });
}

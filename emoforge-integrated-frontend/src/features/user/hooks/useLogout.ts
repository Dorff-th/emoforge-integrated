import { useMutation, useQueryClient} from "@tanstack/react-query";
import { authApi } from "@/features/auth/api/authApi";
import { useAuthStore } from "./useAuthStore";

export const useLogoutMutation = () => {
  const queryClient = useQueryClient();
  const clearAuth = useAuthStore((s) => s.clearAuth);

  return useMutation({
    mutationFn: async () => {
      try {
        await authApi.logout();
      } catch {
        // 이미 세션이 없을 수도 있으므로 무시
      }
    },
    onSettled: () => {
      // 클라이언트 상태 정리
      clearAuth();
      queryClient.clear();
    },
  });
};

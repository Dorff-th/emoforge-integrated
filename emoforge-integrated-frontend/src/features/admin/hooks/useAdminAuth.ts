import { useAdminMe } from "@/features/admin/hooks/useAdminMe";
import { useAdminLogout } from "@/features/admin/hooks/useAdminLogout";

export function useAdminAuth() {
  const meQuery = useAdminMe();
  const logoutMutation = useAdminLogout();

  // 서버에서 role을 "ROLE_ADMIN"으로 내려주면 여기에 맞춰 비교
  // 지금 너는 ROLE_ADMIN을 쓰는 듯 했는데, 가능하면 "ADMIN"으로 통일 추천
  const role = meQuery.data?.role;

  const isAuthorized =
    meQuery.isSuccess && (role === "ADMIN" || role === "ROLE_ADMIN");

  return {
    // guard에서 쓰는 값
    isAuthorized,
    isLoading: meQuery.isLoading,
    isError: meQuery.isError,

    // 화면에서 쓰는 데이터
    adminInfo: meQuery.data ?? null,

    // 로그아웃
    adminLogout: logoutMutation.mutateAsync,
    isLoggingOut: logoutMutation.isPending,
  };
}

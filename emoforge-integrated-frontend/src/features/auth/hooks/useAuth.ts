import { useQuery } from "@tanstack/react-query";
import { authApi } from "../api/authApi";

export function useAuth() {
  const {
    data,
    isLoading,
    isError,
    refetch,
  } = useQuery({
    queryKey: ["auth", "me"],
    queryFn: authApi.me,
    retry: false,
  });

  return {
    user: data ?? null,
    isAuthenticated: !!data,
    isLoading,
    isError,

    // 🔑 OAuth 이후 강제 재조회용
    refetchMe: refetch,
  };
}

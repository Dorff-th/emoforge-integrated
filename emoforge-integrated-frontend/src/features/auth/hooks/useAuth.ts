import { useNavigate } from "react-router-dom";
import { useQuery, useQueryClient } from "@tanstack/react-query";
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

  const queryClient = useQueryClient();

  const navigate = useNavigate();


  const logout = async () => {
    try {
      await authApi.logout();
    } finally {
      queryClient.clear();
      navigate("/login");
    }
  };

  return {
    user: data ?? null,
    isAuthenticated: !!data,
    isLoading,
    isError,
    logout,
    // 🔑 OAuth 이후 강제 재조회용
    refetchMe: refetch,
  };
}

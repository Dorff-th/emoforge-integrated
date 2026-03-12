import { useQuery } from "@tanstack/react-query";
import {
  dashboardKeys,
  fetchDashboardStats,
} from "@/features/admin/api/dashboardApi";

export function useDashboard() {
  return useQuery({
    queryKey: dashboardKeys.all,
    queryFn: fetchDashboardStats,
  });
}

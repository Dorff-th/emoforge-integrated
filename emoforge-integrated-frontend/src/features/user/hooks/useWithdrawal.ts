// hooks/useWithdrawal.ts
import { useMutation } from "@tanstack/react-query";
import { requestWithdrawal } from "@/features/user/api/withdrawalApi";

export const useWithdrawalMutation = () =>
  useMutation({
    mutationFn: requestWithdrawal,
  });

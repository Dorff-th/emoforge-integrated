// hooks/useWithdrawal.ts
import { useMutation } from "@tanstack/react-query";
import { requestWithdrawal, requestWithdrawalCancle } from "@/features/user/api/withdrawalApi";

export const useWithdrawalMutation = () =>
  useMutation({
    mutationFn: requestWithdrawal,
  });

export const useCancelWithdrawalMutation = () => 
  useMutation({
  mutationFn: requestWithdrawalCancle,
});
import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useToast } from "@/shared/stores/useToast";

type AuthErrorKey = "inactive" | "deleted" | "invalid_access" | "signup_failed";

type AuthStatusKey = "unauthorized";

const ERROR_MESSAGE_MAP: Record<AuthErrorKey, string> = {
  inactive: "비활성화된 계정입니다. 관리자에게 문의하세요.",
  deleted: "탈퇴된 계정입니다. 로그인할 수 없습니다.",
  invalid_access: "잘못된 접근입니다. 다시 로그인해주세요.",
  signup_failed: "회원가입 중 문제가 발생했습니다.",
};

const STATUS_MESSAGE_MAP: Record<AuthStatusKey, string> = {
  unauthorized: "로그인이 필요합니다.",
};

export function AuthErrorHandler() {
  const [params, setParams] = useSearchParams();
  const toast = useToast();

  useEffect(() => {
    const error = params.get("error") as AuthErrorKey | null;
    const status = params.get("status") as AuthStatusKey | null;

    let handled = false;

    if (error && ERROR_MESSAGE_MAP[error]) {
      toast.error(ERROR_MESSAGE_MAP[error]);
      handled = true;
    }

    if (status && STATUS_MESSAGE_MAP[status]) {
      toast.error(STATUS_MESSAGE_MAP[status]);
      handled = true;
    }

    if (!handled) return;

    /**
     * 처리한 에러는 URL에서 제거
     * (새로고침 / 뒤로가기 중복 방지)
     */
    const nextParams = new URLSearchParams(params);
    nextParams.delete("error");
    nextParams.delete("status");

    setParams(nextParams, { replace: true });
  }, [params, setParams, toast]);

  return null;
}

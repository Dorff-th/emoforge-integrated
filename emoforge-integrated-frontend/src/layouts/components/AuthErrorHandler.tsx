import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useToast } from "@/shared/stores/useToast";

/**
 * 인증 관련 URL 에러 파라미터를 해석해서
 * 사용자에게 안내만 해주는 컴포넌트
 *
 * - LoginPage 등 개별 페이지는 인증 에러를 몰라도 됨
 * - PublicLayout에 1회만 마운트
 */
export function AuthErrorHandler() {
  const [params, setParams] = useSearchParams();
  const toast = useToast();

  useEffect(() => {
    const error = params.get("error");
    const status = params.get("status");

    if (!error && !status) return;

    // AS-IS에서 사용하던 에러 케이스 흡수
    if (error === "inactive") {
      toast.error("비활성화된 계정입니다. 관리자에게 문의하세요.");
    }

    if (error === "deleted") {
      toast.error("탈퇴된 계정입니다. 로그인할 수 없습니다.");
    }

    if (status === "unauthorized") {
      toast.error("로그인이 필요합니다.");
    }

    if (error === "invalid_access") {
      toast.error("잘못된 접근입니다. 다시 로그인해주세요.");
    }

    if (error === "signup_failed") {
      toast.error("회원가입 중 문제가 발생했습니다.");
    }

    /**
     * 한 번 처리한 에러는 URL에서 제거
     * (뒤로가기 / 새로고침 시 중복 토스트 방지)
     */
    params.delete("error");
    params.delete("status");
    setParams(params, { replace: true });
  }, [params, setParams, toast]);

  return null;
}

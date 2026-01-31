import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "@/features/auth/api/authApi";
import { useAuth } from "@/features/auth/hooks/useAuth";

export function OAuthCallbackPage() {
  const navigate = useNavigate();
  const { refetchMe } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code");

    if (!code) {
      navigate("/login?status=unauthorized", { replace: true });
      return;
    }

    (async () => {
      try {
        // 🔑 핵심: code를 BE로 전달
        await authApi.kakaoLogin(code);

        // 쿠키 세팅 이후 /me 재조회
        try {
          await refetchMe();
        } catch (e: any) {
          if (e.response?.status === 401) {
            // 한번 정도는 재시도 or 대기
            await setTimeout(() => {}, 100);
            await refetchMe();
          } else {
            throw e;
          }
        }

        navigate("/", { replace: true });
      } catch (e) {
        navigate("/login?status=unauthorized", { replace: true });
      }
    })();
  }, [navigate, refetchMe]);

  return (
    <div className="min-h-screen flex items-center justify-center text-slate-600">
      로그인 처리 중입니다…
    </div>
  );
}

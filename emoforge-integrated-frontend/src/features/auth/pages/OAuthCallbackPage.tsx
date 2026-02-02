import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { queryClient } from "@/lib/queryClient";
import { authApi } from "@/features/auth/api/authApi";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { OAuthFlow } from "../api/authFlow";

export function OAuthCallbackPage() {
  const navigate = useNavigate();
  const { refetchMe } = useAuth();
  const ranRef = useRef(false);

  useEffect(() => {
    if (ranRef.current) return;
    ranRef.current = true;

    OAuthFlow.start();

    const run = async () => {
      const params = new URLSearchParams(window.location.search);
      const code = params.get("code");

      if (!code) {
        OAuthFlow.end();
        navigate("/login?status=unauthorized", { replace: true });
        return;
      }

      try {
        // ✅ OAuth 성공의 기준
        // 1) 인가코드 → 백엔드 전달
        const res = await authApi.kakaoLogin(code);
        const data = res.data;

        queryClient.setQueryData(["auth", "kakao"], res.data);

        // 2) 신규 회원 → 약관 동의 필요
        if (res.data.status === "NEED_AGREEMENT") {
          navigate("/auth/terms", {
            replace: true,
            state: {
              kakaoId: res.data.kakaoId,
              nickname: res.data.nickname,
            },
          });
          return;
        }

        // 3) 기존 회원 → 로그인 성공
        if (data.status === "LOGIN_OK") {
          try {
            await refetchMe();
          } catch (e: any) {
            if (e.response?.status === 401) {
              await new Promise((r) => setTimeout(r, 150));
              try {
                await refetchMe();
              } catch {
                // 🔕 여기서는 아무 것도 안 함
                // 로그인 실패 아님
              }
            }
          }
        }

        // ✅ 무조건 홈 이동
        navigate("/", { replace: true });
      } catch {
        // ❌ kakaoLogin 자체가 실패한 경우만
        navigate("/login?status=unauthorized", { replace: true });
      } finally {
        OAuthFlow.end();
      }
    };

    run();
  }, [navigate, refetchMe]);

  return (
    <div className="min-h-screen flex items-center justify-center text-slate-600">
      로그인 처리 중입니다…
    </div>
  );
}

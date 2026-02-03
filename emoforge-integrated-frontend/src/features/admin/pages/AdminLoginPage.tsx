import { useState } from "react";
import { useNavigate } from "react-router-dom";
import ReCAPTCHA from "react-google-recaptcha";

import { adminApi } from "@/features/admin/api/adminApi";
import { useToast } from "@/shared/stores/useToast";

export default function AdminLoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [captchaToken, setCaptchaToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const toast = useToast();

  const SITE_KEY = import.meta.env.VITE_RECAPTCHA_SITE_KEY;

  const handleLogin = async (e: React.SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();

    // 1️⃣ FE 1차 검증
    if (!username || !password) {
      toast.error("아이디와 비밀번호를 입력해주세요");
      return;
    }

    if (!captchaToken) {
      toast.error("reCAPTCHA 인증이 필요합니다.");
      return;
    }

    try {
      setLoading(true);

      const res = await adminApi.adminLogin({
        username,
        password,
        captchaToken,
      });

      console.log(res);

      // 2️⃣ 성공 시 (access_token 쿠키 생성됨)
      if (res.status === 200) {
        toast.success("로그인에 성공했습니다.");
        navigate("/admin/dashboard");
      }
    } catch (err: any) {
      toast.error(err.response?.data?.message ?? "로그인에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded-xl shadow-md w-96"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">관리자 로그인</h2>

        <div className="mb-4">
          <input
            type="text"
            placeholder="아이디"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full border rounded p-2"
            disabled={loading}
          />
        </div>

        <div className="mb-4">
          <input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full border rounded p-2"
            disabled={loading}
          />
        </div>

        {/* reCAPTCHA */}
        <div className="flex justify-center mb-4">
          <ReCAPTCHA
            sitekey={SITE_KEY}
            onChange={(token) => setCaptchaToken(token)}
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "로그인 중..." : "로그인"}
        </button>
      </form>
    </div>
  );
}

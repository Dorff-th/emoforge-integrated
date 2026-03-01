// src/features/auth/pages/LoginPage.tsx
import { redirectToKakaoLogin } from "@/shared/utils/redirectToKakaoLogin";

export default function LoginPage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-[#fdfcfb] to-[#e2ebf0] flex items-center justify-center">
      <div className="text-center px-6">
        {/* 로고 + 타이틀 */}
        <div className="mb-10">
          <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-white/80 shadow-sm">
            <span className="text-3xl font-extrabold text-slate-800">EF</span>
          </div>
          <h1 className="text-3xl font-semibold text-slate-900 tracking-tight">
            Emoforge
          </h1>
          <p className="mt-3 text-sm md:text-base text-slate-600">
            하루를 적고, 잠시 생각에 잠겨 봅시다.
          </p>
        </div>

        {/* 카카오 로그인 버튼 */}
        <button
          type="button"
          onClick={redirectToKakaoLogin}
          className="inline-flex items-center gap-2 rounded-full bg-[#FEE500] px-8 py-3 text-sm font-semibold text-slate-900 shadow-md shadow-yellow-500/40 hover:shadow-lg hover:-translate-y-0.5 active:translate-y-0 transition-all"
        >
          <span className="flex h-7 w-7 items-center justify-center rounded-full border border-black/15 bg-[#FFEB3B] text-base">
            🙂
          </span>
          <span>카카오 로그인</span>
        </button>

        {/* 푸터 */}
        <p className="mt-10 text-[11px] tracking-[0.24em] text-slate-400 uppercase">
          © {new Date().getFullYear()} Emoforge
        </p>
      </div>
    </div>
  );
}

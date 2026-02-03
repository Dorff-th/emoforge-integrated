import { useUILoading } from "@/shared/stores/useUILoading";

export default function UserHomePage() {
  useUILoading("user:home", { duration: 300 });

  return (
    <div className="rounded-lg bg-white p-6 shadow-sm">
      <h1 className="text-xl font-semibold text-slate-900">로그인 성공 🎉</h1>
      <p className="mt-2 text-sm text-slate-600">
        여기가 로그인 이후 첫 화면(Home)입니다.
      </p>
    </div>
  );
}

import { useAdminAuth } from "@/features/admin/hooks/useAdminAuth";
export default function AdminHeader() {
  const { adminInfo, adminLogout, isLoggingOut } = useAdminAuth();

  return (
    <header className="flex justify-between items-center bg-white shadow px-6 py-3">
      <h1 className="text-lg font-semibold text-gray-800">🛠 Admin Console</h1>
      <span className="text-sm text-gray-600">
        {adminInfo?.nickname ?? "관리자"}
      </span>
      <button
        onClick={() => adminLogout()}
        className="bg-red-500 text-white px-4 py-1.5 rounded hover:bg-red-600"
      >
        {isLoggingOut ? "로그아웃 중..." : "로그아웃"}
      </button>
    </header>
  );
}

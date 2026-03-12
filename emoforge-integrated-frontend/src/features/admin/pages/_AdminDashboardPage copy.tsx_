import { useAdminAuth } from "@/features/admin/hooks/useAdminAuth";

export default function AdminDashboardPage() {
  const { adminInfo, isLoading, isError } = useAdminAuth();

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-xl shadow-md w-[480px] text-center">
        <h1 className="text-2xl font-bold mb-4">관리자 대시보드</h1>

        {isLoading ? (
          <p className="text-gray-400">관리자 정보를 불러오는 중...</p>
        ) : isError ? (
          <p className="text-red-500">인증 실패</p>
        ) : adminInfo ? (
          <>
            <p className="text-gray-700 mb-2">
              <b>관리자 계정:</b> {adminInfo.nickname}
            </p>
            <p className="text-gray-700 mb-4">
              <b>권한:</b> {adminInfo.role}
            </p>
            <p className="text-green-600 font-semibold">{adminInfo.message}</p>
          </>
        ) : null}
      </div>
    </div>
  );
}

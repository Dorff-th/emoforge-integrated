export function AdminAuthLoading() {
  return (
    <div className="flex h-screen items-center justify-center">
      <div className="flex flex-col items-center gap-3">
        <div className="h-10 w-10 animate-spin rounded-full border-4 border-t-transparent border-gray-700" />
        <p className="text-sm text-gray-600">관리자 권한 확인 중...</p>
      </div>
    </div>
  );
}

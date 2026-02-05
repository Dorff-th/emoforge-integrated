export default function ProfileImageSection() {
  return (
    <section className="flex items-center gap-6">
      <div className="relative">
        <div className="w-24 h-24 rounded-full bg-gray-200" />
        <button className="absolute bottom-0 right-0 w-8 h-8 rounded-full bg-white border flex items-center justify-center">
          ⚙️
        </button>
      </div>

      <div>
        <p className="text-sm font-medium">프로필 이미지</p>
        <p className="text-xs text-gray-500">최신 이미지 1개만 사용됩니다</p>
      </div>
    </section>
  );
}

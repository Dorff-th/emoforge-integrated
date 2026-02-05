export default function ProfileInfoSection() {
  return (
    <section className="space-y-4">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-gray-500">닉네임</p>
          <p className="font-medium">이태호</p>
        </div>
        <button className="text-sm underline">수정</button>
      </div>

      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-gray-500">이메일</p>
          <p className="font-medium">roguelove79@daum.net</p>
        </div>
        <button className="text-sm underline">수정</button>
      </div>
    </section>
  );
}

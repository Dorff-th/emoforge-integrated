export default function ProfileImageUploadModal() {
  return (
    <div>
      <h2 className="text-lg font-semibold">프로필 이미지 업로드</h2>

      <input type="file" />

      <div className="flex justify-end gap-2 mt-4">
        <button>취소</button>
        <button className="font-medium">업로드</button>
      </div>
    </div>
  );
}

export default function DiaryPreviewCard() {
  return (
    <div className="rounded-xl p-4 bg-purple-50 space-y-2">
      <p className="text-sm font-medium">오늘의 회고</p>

      <p className="text-sm text-gray-700 line-clamp-3">
        입춘.. 봄 but 극심한 미세먼지 글루글루 노답계절
      </p>

      {/* <button className="text-xs underline">회고 수정하기</button> 이미 기록된 회고는 수정 기능없음, 단 삭제 가능. 그리고 오늘의 회고는 계속 기록가능 */}
    </div>
  );
}

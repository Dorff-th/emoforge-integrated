export default function BoardSection() {
  return (
    <section className="py-24 px-6">
      <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-12 items-center">
        <img
          src="/images/intro/post-detail.png"
          alt="게시판 상세"
          className="rounded-xl shadow-md border"
        />

        <div>
          <h2 className="text-2xl md:text-3xl font-semibold mb-6">
            게시판 기능
          </h2>

          <ul className="space-y-3 text-muted-foreground leading-relaxed">
            <li>• 게시글 작성 / 수정 / 삭제</li>
            <li>• 댓글 기능</li>
            <li>• 파일 첨부</li>
            <li>• 통합 검색 기능</li>
          </ul>
        </div>
      </div>
    </section>
  );
}

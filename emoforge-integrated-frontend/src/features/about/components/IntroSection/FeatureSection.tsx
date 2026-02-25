import FeatureCard from "@/features/about/components/IntroSection/FeatureCard";

export default function FeatureSection() {
  return (
    <section className="py-20 px-6 bg-muted/30">
      <div className="max-w-6xl mx-auto">
        <h2 className="text-2xl md:text-3xl font-semibold text-center mb-12">
          핵심 기능
        </h2>

        <div className="grid md:grid-cols-3 gap-8">
          <FeatureCard
            title="감정 & 회고"
            description="하루 감정을 기록하고 GPT 피드백을 통해 스스로를 돌아볼 수 있습니다."
            image="/images/intro/diary_write.png"
          />
          <FeatureCard
            title="게시판"
            description="게시글 작성, 댓글, 파일 첨부 및 통합 검색 기능을 제공합니다."
            image="/images/intro/posts.png"
          />
          <FeatureCard
            title="OAuth2 인증"
            description="카카오 로그인 기반 JWT 인증 구조를 적용하였습니다."
            image="/images/intro/login.png"
          />
        </div>
      </div>
    </section>
  );
}

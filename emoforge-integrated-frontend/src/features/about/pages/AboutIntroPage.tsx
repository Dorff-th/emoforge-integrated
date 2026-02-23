import FeatureSlider from "@/features/about/components/FeatureSlider/FeatureSlider";

const AboutIntroPage = () => {
  return (
    <main className="w-full">
      {/* Intro 텍스트 영역 */}
      <section className="max-w-4xl mx-auto px-4 pt-20 pb-12 text-center space-y-4">
        <h1 className="text-3xl font-bold tracking-tight">Emoforge</h1>

        <p className="text-base text-gray-500">
          감정 기록과 회고를 중심으로 설계된 개인 프로젝트입니다.
        </p>
      </section>

      {/* 기능 슬라이더 */}
      <section className="pb-20">
        <FeatureSlider />
      </section>

      {/* (선택) 다음 페이지 유도 */}
      <section className="text-center pb-16">
        <a
          href="/about/emoforge"
          className="text-sm text-gray-500 hover:text-gray-800 transition"
        >
          Emoforge 구조 자세히 보기 →
        </a>
      </section>
    </main>
  );
};

export default AboutIntroPage;

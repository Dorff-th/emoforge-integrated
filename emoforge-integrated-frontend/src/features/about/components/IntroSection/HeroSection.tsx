import { Link } from "react-router-dom";

export default function HeroSection() {
  return (
    <section className="py-24 px-6 text-center max-w-4xl mx-auto">
      <h1 className="text-4xl md:text-5xl font-bold mb-6">Emoforge</h1>

      <p className="text-lg md:text-xl text-muted-foreground mb-8 leading-relaxed">
        감정 기록과 설계 실험이 결합된 개인 프로젝트입니다. MSA로 시작해
        Monolith로 통합된 Full Stack 웹 플랫폼입니다.
      </p>

      <div className="flex justify-center gap-4">
        <Link
          to="/login"
          target="_blank"
          className="px-6 py-3 rounded-lg bg-primary text-white hover:opacity-90 transition"
        >
          로그인하여 체험하기
        </Link>
        <Link
          to="https://github.com/Dorff-th/emoforge-integrated"
          target="_blank"
          className="px-6 py-3 rounded-lg border hover:bg-muted transition"
        >
          GitHub 보기
        </Link>
      </div>
    </section>
  );
}

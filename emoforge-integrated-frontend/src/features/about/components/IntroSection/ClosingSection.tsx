import { Link } from "react-router-dom";

export default function ClosingSection() {
  return (
    <section className="py-24 px-6 text-center">
      <h2 className="text-2xl md:text-3xl font-semibold mb-6">
        설계와 운영까지 고려한 개인 프로젝트
      </h2>

      <p className="text-muted-foreground mb-8">
        기능 구현을 넘어 아키텍처 설계, CI/CD, 운영 안정성까지 직접 경험하며
        구축하였습니다.
      </p>

      <Link
        className="px-6 py-3 rounded-lg bg-primary text-white hover:opacity-90 transition"
        to="/login"
        target="_blank"
      >
        지금 체험해보기
      </Link>
    </section>
  );
}

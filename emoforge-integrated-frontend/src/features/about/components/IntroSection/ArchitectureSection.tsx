export default function ArchitectureSection() {
  return (
    <section className="py-20 px-6 bg-muted/30">
      <div className="max-w-4xl mx-auto text-center">
        <h2 className="text-2xl md:text-3xl font-semibold mb-6">
          MSA → Monolith 전환
        </h2>

        <p className="text-muted-foreground mb-10 leading-relaxed">
          초기에는 Cloud Native 실험을 위해 MSA 구조로 설계하였습니다. 이후 운영
          복잡도와 리소스 효율을 고려하여 Monolith 구조로 통합하였습니다.
        </p>

        <div className="inline-block border rounded-lg overflow-hidden">
          <table className="text-sm">
            <thead className="bg-muted">
              <tr>
                <th className="px-6 py-3">구분</th>
                <th className="px-6 py-3">MSA</th>
                <th className="px-6 py-3">Monolith</th>
              </tr>
            </thead>
            <tbody>
              <tr className="border-t">
                <td className="px-6 py-3">컨테이너 수</td>
                <td className="px-6 py-3">11</td>
                <td className="px-6 py-3">4</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}

import ArchitectureToggleSection from "@/features/about/components/ArchitectureToggleSection";

export default function PortfolioPage() {
  return (
    <div className="space-y-24">
      {/* Hero */}
      <section className="text-center space-y-6">
        <h2 className="text-3xl font-bold">Architecture Evolution</h2>

        <p className="text-muted-foreground max-w-2xl mx-auto">
          11 Containers → 4 Containers · swap 1.4GB → 160MB · Manual Deploy →
          CI/CD
        </p>
      </section>

      {/* Interactive Architecture */}
      <ArchitectureToggleSection />

      {/* Five Line Summary */}
      <section className="bg-muted rounded-xl p-8">
        <h3 className="font-semibold mb-4">5-Line Summary</h3>

        <ul className="space-y-2 text-sm">
          <li>Emoforge began as an MSA experiment with 12 containers.</li>
          <li>Resource limits exposed operational complexity.</li>
          <li>Monolith integration reduced infrastructure overhead.</li>
          <li>Memory stabilized and CI/CD was introduced.</li>
          <li>MSA is a strategy, not a goal.</li>
        </ul>
      </section>
      {/* Snapshot Section */}
      <section className="space-y-16">
        <div>
          <h3 className="text-2xl font-semibold mb-4">Operational Snapshot</h3>

          <p className="text-muted-foreground text-sm mb-8">
            Snapshot captured from actual EC2 production environment.
          </p>
        </div>

        {/* AS-IS */}
        <div className="space-y-6">
          <div>
            <h4 className="text-lg font-semibold text-red-500">AS-IS (MSA)</h4>
            <p className="text-sm text-muted-foreground mt-1">
              11 containers · 699MB memory used · 1.4GB swap used
            </p>
          </div>

          <div className="grid md:grid-cols-2 gap-6">
            <img
              src="/images/msa-docker.png"
              alt="MSA docker ps"
              className="rounded-lg border"
            />
            <img
              src="/images/msa-memory.png"
              alt="MSA free -h"
              className="rounded-lg border"
            />
          </div>
        </div>

        {/* TO-BE */}
        <div className="space-y-6">
          <div>
            <h4 className="text-lg font-semibold text-emerald-500">
              TO-BE (Monolith)
            </h4>
            <p className="text-sm text-muted-foreground mt-1">
              4 containers · 531MB memory used · 160MB swap used
            </p>
          </div>

          <div className="grid md:grid-cols-2 gap-6">
            <img
              src="/images/mono-docker.png"
              alt="Monolith docker ps"
              className="rounded-lg border"
            />
            <img
              src="/images/mono-memory.png"
              alt="Monolith free -h"
              className="rounded-lg border"
            />
          </div>
        </div>
      </section>
    </div>
  );
}

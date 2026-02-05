export default function ProfileStatsSection() {
  return (
    <section className="space-y-4">
      <h2 className="text-lg font-semibold">Profile Statistics</h2>

      <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
        <StatCard label="Records" value={22} />
        <StatCard label="GPT Summary" value={6} />
        <StatCard label="Posts" value={5} />
        <StatCard label="Comments" value={5} />
      </div>
    </section>
  );
}

function StatCard({ label, value }: { label: string; value: number }) {
  return (
    <div className="rounded-xl border p-4 text-center space-y-1">
      <p className="text-xl font-semibold">{value}</p>
      <p className="text-sm text-gray-500">{label}</p>
    </div>
  );
}

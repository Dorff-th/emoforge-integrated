import DiaryForm from "../components/DiaryForm";

export default function DiaryWritePage() {
  return (
    <div className="w-full max-w-3xl rounded-xl mx-auto bg-white p-2 shadow-lg">
      {/* <div className="bg-white rounded-2xl shadow-sm p-6">  */}
      <div className="min-h-screen bg-[var(--bg)] text-[var(--text)] transition-colors">
        <DiaryForm />
      </div>
    </div>
  );
}

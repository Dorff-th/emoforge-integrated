import "./App.css";

function App() {
  return (
    <>
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-r from-blue-400 to-purple-500">
        <div className="rounded-2xl bg-white p-10 shadow-xl">
          <h1 className="text-3xl font-bold text-gray-800 mb-4">
            🎉 Tailwind 설치 성공!
          </h1>
          <p className="text-gray-600">
            이 문장이 보인다면 Tailwind가 정상적으로 동작 중입니다!! 🚀
          </p>
          <button className="mt-6 rounded-lg bg-blue-500 px-4 py-2 font-semibold text-white shadow hover:bg-blue-600 transition">
            확인 버튼
          </button>
        </div>
      </div>
    </>
  );
}

export default App;

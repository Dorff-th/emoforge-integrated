import { useState } from "react";
import { slideGroups } from "./slides";
import ImageCarousel from "./ImageCarousel";

const FeatureSlider = () => {
  const [groupIndex, setGroupIndex] = useState(0);

  const current = slideGroups[groupIndex];

  const prevGroup = () => setGroupIndex((i) => Math.max(i - 1, 0));

  const nextGroup = () =>
    setGroupIndex((i) => Math.min(i + 1, slideGroups.length - 1));

  return (
    <section className="w-full max-w-4xl mx-auto space-y-4">
      {/* 제목 */}
      <div className="text-center space-y-1">
        <h2 className="text-xl font-semibold">{current.title}</h2>
        <p className="text-sm text-gray-500">{current.description}</p>
      </div>

      {/* 이미지 */}
      <ImageCarousel key={current.id} images={current.images} />

      {/* 그룹 네비게이션 */}
      <div className="flex justify-between items-center">
        <button
          onClick={prevGroup}
          disabled={groupIndex === 0}
          className="px-3 py-1 text-sm disabled:opacity-30"
        >
          이전
        </button>

        <div className="flex gap-2">
          {slideGroups.map((_, i) => (
            <button
              key={i}
              aria-label={`${i + 1}번 기능`}
              onClick={() => setGroupIndex(i)}
              className={`w-2.5 h-2.5 rounded-full ${
                i === groupIndex ? "bg-gray-800" : "bg-gray-300"
              }`}
            />
          ))}
        </div>

        <button
          onClick={nextGroup}
          disabled={groupIndex === slideGroups.length - 1}
          className="px-3 py-1 text-sm disabled:opacity-30"
        >
          다음
        </button>
      </div>
    </section>
  );
};

export default FeatureSlider;

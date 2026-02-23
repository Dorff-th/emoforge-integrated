import { useState, useRef, useEffect } from "react";

type Props = {
  images: { src: string; alt: string }[];
};

const SWIPE_THRESHOLD = 50;

const ImageCarousel = ({ images }: Props) => {
  const [index, setIndex] = useState(0);
  const startX = useRef<number | null>(null);

  useEffect(() => {
    setIndex(0);
  }, [images]);

  const hasMultiple = images.length > 1;

  const prev = () => setIndex((i) => Math.max(i - 1, 0));
  const next = () => setIndex((i) => Math.min(i + 1, images.length - 1));

  const onTouchStart = (e: React.TouchEvent) => {
    startX.current = e.touches[0].clientX;
  };

  const onTouchEnd = (e: React.TouchEvent) => {
    if (startX.current === null) return;

    const endX = e.changedTouches[0].clientX;
    const delta = startX.current - endX;

    if (Math.abs(delta) > SWIPE_THRESHOLD) {
      delta > 0 ? next() : prev();
    }

    startX.current = null;
  };

  return (
    <div
      className="relative w-full aspect-video overflow-hidden rounded-lg bg-black"
      onTouchStart={hasMultiple ? onTouchStart : undefined}
      onTouchEnd={hasMultiple ? onTouchEnd : undefined}
    >
      <img
        src={images[index].src}
        alt={images[index].alt}
        className="w-full h-full object-contain select-none"
        draggable={false}
      />

      {hasMultiple && (
        <>
          <button
            onClick={prev}
            aria-label="이전 화면"
            className="absolute left-2 top-1/2 -translate-y-1/2 bg-black/50 text-white px-2 py-1 rounded"
          >
            ‹
          </button>

          <button
            onClick={next}
            aria-label="다음 화면"
            className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/50 text-white px-2 py-1 rounded"
          >
            ›
          </button>

          <div className="absolute bottom-2 left-1/2 -translate-x-1/2 flex gap-1">
            {images.map((_, i) => (
              <button
                key={i}
                aria-label={`${i + 1}번 화면`}
                onClick={() => setIndex(i)}
                className={`w-2 h-2 rounded-full ${
                  i === index ? "bg-white" : "bg-white/40"
                }`}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default ImageCarousel;

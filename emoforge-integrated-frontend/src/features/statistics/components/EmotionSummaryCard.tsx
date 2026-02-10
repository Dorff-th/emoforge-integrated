interface Props {
  average: number;
}

const EmotionSummaryCard = ({ average }: Props) => {
  const { emoji, message } = getEmotionVisual(average);

  return (
    <div className="bg-yellow-100 border border-yellow-300 rounded-xl p-4 shadow-md text-center">
      <div className="text-4xl mb-2">{emoji}</div>
      <div className="text-xl font-semibold">
        평균 감정: {average.toFixed(2)}
      </div>
      <p className="text-sm text-gray-700 mt-1">{message}</p>
    </div>
  );
};

const getEmotionVisual = (score: number) => {
  if (score >= 4.5) return { emoji: "🤩", message: "최고의 기분이에요!" };
  if (score >= 4.0)
    return { emoji: "😄", message: "기분 좋은 날들이 많았어요!" };
  if (score >= 3.0) return { emoji: "🙂", message: "평범한 하루들이었어요." };
  if (score >= 2.0) return { emoji: "😐", message: "조금 지쳤을지도 몰라요." };
  return { emoji: "😢", message: "기운 내요. 내일은 더 나을 거예요!" };
};

export default EmotionSummaryCard;

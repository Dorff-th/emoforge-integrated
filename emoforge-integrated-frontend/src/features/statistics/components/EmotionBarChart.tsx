import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";

const EmotionBarChart = ({
  frequency,
}: {
  frequency: Record<number, number>;
}) => {
  const data = Object.entries(frequency).map(([score, count]) => ({
    score,
    count,
  }));

  return (
    <div>
      <h3 className="font-semibold">감정 점수 빈도</h3>
      <BarChart width={350} height={200} data={data}>
        <XAxis dataKey="score" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="count" />
      </BarChart>
    </div>
  );
};

export default EmotionBarChart;

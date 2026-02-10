import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

interface Props {
  trend: {
    weekLabel: string;
    averageEmotion: number;
  }[];
}

const WeeklyLineChart = ({ trend }: Props) => {
  return (
    <div>
      <h3 className="font-semibold mb-2">주간 평균 감정 추이</h3>
      <LineChart width={350} height={250} data={trend}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="weekLabel" />
        <YAxis domain={[1, 5]} tickCount={5} />
        <Tooltip />
        <Line type="monotone" dataKey="averageEmotion" stroke="#8884d8" />
      </LineChart>
    </div>
  );
};

export default WeeklyLineChart;

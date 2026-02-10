import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";

interface Props {
  dayAverage: Record<string, number>;
}

const dayOrder = [
  "MONDAY",
  "TUESDAY",
  "WEDNESDAY",
  "THURSDAY",
  "FRIDAY",
  "SATURDAY",
  "SUNDAY",
];

const DayOfWeekBarChart = ({ dayAverage }: Props) => {
  const data = dayOrder.map((day) => ({
    day,
    average: dayAverage[day] || 0,
  }));

  return (
    <div>
      <h3 className="font-semibold mb-2">요일별 평균 감정</h3>
      <BarChart width={350} height={200} data={data}>
        <XAxis dataKey="day" />
        <YAxis domain={[1, 5]} tickCount={5} />
        <Tooltip />
        <Bar dataKey="average" fill="#82ca9d" />
      </BarChart>
    </div>
  );
};

export default DayOfWeekBarChart;

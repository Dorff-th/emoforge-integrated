import React from "react";
import Slider from "@mui/material/Slider";

interface Props {
  value: number[];
  onChange: (value: [number, number]) => void;
}

const emotionMarks = [
  { value: 1, label: "😞" },
  { value: 2, label: "😐" },
  { value: 3, label: "🙂" },
  { value: 4, label: "😊" },
  { value: 5, label: "😄" },
];
const EmotionRangeSlider: React.FC<Props> = ({ value, onChange }) => {
  return (
    <div className="px-4 py-2 w-full">
      <Slider
        value={value}
        onChange={(e, newValue) => onChange(newValue as [number, number])}
        valueLabelDisplay="auto"
        min={1}
        max={5}
        step={1}
        marks={emotionMarks} // 👈 요기!
      />
    </div>
  );
};

export default EmotionRangeSlider;

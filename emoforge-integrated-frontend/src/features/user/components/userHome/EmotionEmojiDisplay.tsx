import {
  type EmotionLevel,
  emotionEmojiMap,
} from "@/shared/constants/emotionMap";

interface EmotionEmojiDisplayProps {
  emotion?: EmotionLevel;
  size?: "sm" | "md" | "lg";
}

export function EmotionEmojiDisplay({
  emotion,
  size = "md",
}: EmotionEmojiDisplayProps) {
  const sizeClass =
    size === "lg" ? "text-4xl" : size === "sm" ? "text-xl" : "text-2xl";

  return (
    <span className={sizeClass}>
      {emotion ? emotionEmojiMap[emotion] : "🙂"}
    </span>
  );
}

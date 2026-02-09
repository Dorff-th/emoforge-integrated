// shared/utils/emotion.ts
import { type EmotionLevel } from "@/shared/constants/emotionMap";

export function toEmotionLevel(
  value?: number
): EmotionLevel | undefined {
  if (!value) return undefined;

  const rounded = Math.round(value);

  if (rounded < 1) return 1;
  if (rounded > 5) return 5;

  return rounded as EmotionLevel;
}

export type EmotionLevel = 1 | 2 | 3 | 4 | 5;

export interface EmotionItem {
  value: EmotionLevel;
  emoji: string;
  label: string;
}

export const emotionList: EmotionItem[] = [
  { value: 1, emoji: '😢', label: '슬픔' },
  { value: 2, emoji: '😐', label: '무덤덤' },
  { value: 3, emoji: '🙂', label: '보통' },
  { value: 4, emoji: '😄', label: '좋음' },
  { value: 5, emoji: '🤩', label: '최고' },
];

// value 기준으로 이모지 반환
export const emotionEmojiMap: Record<EmotionLevel, string> = emotionList.reduce(
  (map, item) => {
    map[item.value] = item.emoji;
    return map;
  },
  {} as Record<EmotionLevel, string>
);

// value 기준으로 label 반환
export const emotionLabelMap: Record<EmotionLevel, string> = emotionList.reduce(
  (map, item) => {
    map[item.value] = item.label;
    return map;
  },
  {} as Record<EmotionLevel, string>
);

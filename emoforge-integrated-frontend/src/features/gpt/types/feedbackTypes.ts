// types/feedbackTypes.ts
export type FeedbackType = 'encourage' | 'scold' | 'roast' | 'random' | 'default';

export interface FeedbackStyle {
  emoji: string;
  bgColor: string;
  borderColor: string;
  label: string;
}

export const feedbackStyleMap: Record<FeedbackType, FeedbackStyle> = {
  encourage: {
    emoji: '🧸',
    bgColor: 'bg-blue-100',
    borderColor: 'border-blue-300',
    label: '격려',
  },
  scold: {
    emoji: '🔥',
    bgColor: 'bg-red-100',
    borderColor: 'border-red-300',
    label: '갈굼',
  },
  roast: {
    emoji: '😅',
    bgColor: 'bg-yellow-100',
    borderColor: 'border-yellow-400',
    label: '혼냄',
  },
  random: {
    emoji: '🎲',
    bgColor: 'bg-gray-100',
    borderColor: 'border-gray-300',
    label: '랜덤',
  },
  default: {
    emoji: '🎲',
    bgColor: 'bg-gray-100',
    borderColor: 'border-gray-300',
    label: '기본',
  },
};

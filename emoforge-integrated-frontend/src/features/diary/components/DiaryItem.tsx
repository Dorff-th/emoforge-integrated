import { motion, AnimatePresence } from 'framer-motion';
import { DiaryEntry } from '@/features/diary/api/diaryApi'; // 변경됨
import DiaryDetail from './DiaryDetail';
import { emotionEmojiMap, emotionLabelMap, EmotionLevel } from '@/features/diary/types/emotionMap';

interface DiaryItemProps {
  diary: DiaryEntry;
  isOpen: boolean;
  onToggle: () => void;
}

export default function DiaryItem({ diary, isOpen, onToggle }: DiaryItemProps) {
  const emotionValue = Number(diary.emotion);
  const emotionEmoji = emotionEmojiMap[emotionValue as EmotionLevel] ?? '😶';
  const emotionLabel = emotionLabelMap[emotionValue as EmotionLevel] ?? '';

  return (
    <div
      className="bg-gradient-to-r from-blue-50 to-white rounded-xl shadow-md border p-4 cursor-pointer transition-transform hover:scale-[1.02]"
      onClick={onToggle}
    >
      <div className="flex items-center justify-between">
        <div>
          <p className="text-lg font-semibold">
            {new Date(diary.createdAt).toLocaleDateString()}
          </p>
          <p className="text-sm text-gray-700 dark:text-gray-300 line-clamp-2">
            {diary.content}
          </p>
        </div>
        <div className="flex items-center gap-2 text-right">
          <span className="text-2xl">{emotionEmoji}</span>
          <span className="text-sm text-gray-500">{emotionLabel}</span>
        </div>
      </div>

      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: 'auto', opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ duration: 0.3 }}
          >
            <DiaryDetail diary={diary} />
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

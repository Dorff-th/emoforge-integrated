import { useState } from 'react';
import { emotionEmojiMap, EmotionLevel } from '@/features/diary/types/emotionMap';
import { X, Trash2 } from 'lucide-react';
import { DiaryEntry } from '@/features/calendar/api/calendarApi';
import { generateGptSummary } from '@/features/gpt/api/gptSummaryApi';
import { deleteDiaryEntry, deleteGptSummaryOnly } from '@/features/diary/api/diaryApi'; // ✅ 추가
import { getToastHelper } from '@/features/toast/utils/toastHelper';
import { MusicRecommendModal } from '@/features/music/components/MusicRecommendModal';
import ConfirmDialog from '@/features/ui/components/ConfirmDialog';

interface DiaryListForDateModalProps {
  date: string;
  onClose: () => void;
  diaryEntries?: DiaryEntry[];
  summary: string;
  onSummaryDeleted?: (date: string) => void; // ✅ 수정
  onSummaryGenerated?: (newSummary: string | null) => void; // ✅ 수정!
}

const DiaryListForDateModal = ({
  date,
  onClose,
  diaryEntries,
  summary,
  onSummaryDeleted,
  onSummaryGenerated,
}: DiaryListForDateModalProps) => {
  const [openEntryId, setOpenEntryId] = useState<number | null>(null);
  const [gptSummary, setGptSummary] = useState<string | null>(summary || null);
  const [loading, setLoading] = useState(false);
  const [entries, setEntries] = useState(diaryEntries ?? []);

  // ✅ 타입 통일: id는 number
  type PendingDeleteState = { type: 'gpt' } | { type: 'diary'; id: number };
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [pendingDelete, setPendingDelete] = useState<PendingDeleteState | null>(null);

  const toast = getToastHelper();

  const handleGptSummaryClick = async () => {
    try {
      setLoading(true);
      const result = await generateGptSummary(date);
      setGptSummary(result);
      onSummaryGenerated?.(result);
      toast?.showToast({ message: 'GPT 요약 생성 완료!', type: 'info' });
    } catch (error) {
      toast?.showToast({ message: 'GPT 요약 생성 중 오류 발생!', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const handleGptSummaryDeleteClick = () => {
    setPendingDelete({ type: 'gpt' });
    setConfirmOpen(true);
  };

  const handleDiaryDeleteClick = (entryId: number) => {
    setPendingDelete({ type: 'diary', id: entryId });
    setConfirmOpen(true);
  };

  const handleDiaryDelete = async (entryId: number) => {
    try {
      await deleteDiaryEntry(entryId, false);
      setEntries((prev) => prev.filter((e) => e.id !== entryId));
      toast?.showToast({ message: '회고가 삭제되었습니다.', type: 'success' });
    } catch (error) {
      toast?.showToast({ message: '회고 삭제 중 오류 발생', type: 'error' });
    }
  };

  const handleConfirmDelete = async () => {
    if (!pendingDelete) return;
    setLoading(true); // ✅ 로딩 시작

    try {
      if (pendingDelete.type === 'gpt') {
        await deleteGptSummaryOnly(date);
        setGptSummary(null);
        onSummaryDeleted?.(date); // ✅ 부모 상태 갱신
        toast?.showToast({ message: 'GPT 요약이 삭제되었습니다.', type: 'success' });
      } else if (pendingDelete.type === 'diary' && pendingDelete.id) {
        await handleDiaryDelete(pendingDelete.id);
      }
    } finally {
      setLoading(false);
      setConfirmOpen(false);
      setPendingDelete(null);
    }
  };

  const parsedHabits = (habitTags: string) => {
    try {
      return JSON.parse(habitTags);
    } catch {
      return [];
    }
  };

  const [openMusicModal, setOpenMusicModal] = useState(false);
  const [selectedDiaryId, setSelectedDiaryId] = useState<number | null>(null);

  const sortedList = [...entries].sort((a, b) => b.id - a.id);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white rounded-xl shadow-lg max-w-xl w-full p-6 relative">
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-400 hover:text-black">
          <X size={24} />
        </button>

        <h2 className="text-xl font-bold mb-4 flex justify-between items-center">
          📅 {date}
        </h2>

        {/* ✅ GPT 요약 영역 */}
        {gptSummary ? (
          <div className="mb-4 p-3 bg-yellow-100 text-sm rounded leading-relaxed text-yellow-800 relative">
            <strong>GPT 요약:</strong> {gptSummary}
            <button
              onClick={handleGptSummaryDeleteClick}
              className="absolute top-2 right-2 text-red-500 hover:text-red-700"
              title="GPT 요약 삭제"
            >
              <Trash2 size={16} />
            </button>
          </div>
        ) : (
          <button
            onClick={handleGptSummaryClick}
            disabled={loading}
            className="mb-4 px-4 py-2 bg-blue-600 text-white text-sm rounded hover:bg-blue-700"
          >
            {loading ? 'GPT 요약 생성 중...' : '🧠 GPT 요약 생성'}
          </button>
        )}

        {/* ✅ 회고 리스트 */}
        {sortedList.map((entry, idx) => (
          <div key={entry.id} className="mb-4 border rounded-md p-3 relative">
            {/* 🗑️ 회고 삭제 버튼 */}
            <button
              onClick={() => handleDiaryDeleteClick(entry.id)}
              className="absolute top-2 right-2 text-red-500 hover:text-red-700"
              title="회고 삭제"
            >
              <Trash2 size={16} />
            </button>

            <button
              className="w-full text-left font-semibold text-blue-900 hover:underline"
              onClick={() => setOpenEntryId(openEntryId === entry.id ? null : entry.id)}
            >
              Diary Entry {sortedList.length - idx}
            </button>

            {openEntryId === entry.id && (
              <div className="mt-3 space-y-2 text-sm text-gray-800">
                <div>
                  😊 감정 상태:{' '}
                  <span className="text-xl">
                    {emotionEmojiMap[entry.emotion as EmotionLevel]}
                  </span>
                </div>
                <div>✅ 오늘의 습관: {parsedHabits(entry.habitTags).join(', ') || '없음'}</div>
                <div>💬 오늘의 기분 한마디: {entry.feelingKo} / <i>{entry.feelingEn}</i></div>
                <div>📝 회고: {entry.content}</div>
                <div>🤖 GPT 피드백: {entry.feedback}</div>
              </div>
            )}

            <div className="mt-3">
              <button
                onClick={() => {
                  setSelectedDiaryId(entry.id);
                  setOpenMusicModal(true);
                }}
                className="px-3 py-1 bg-pink-500 text-white text-xs rounded hover:bg-pink-600"
              >
                🎵 음악 추천
              </button>
            </div>
          </div>
        ))}

        {sortedList.length === 0 && (
          <p className="text-center text-gray-500">해당 날짜에 작성된 회고가 없습니다.</p>
        )}
      </div>

      {/* ✅ 수정된 부분: ConfirmDialog를 GPT 요약 블록 바깥으로 이동 (항상 렌더링되도록) */}
      <ConfirmDialog
        open={confirmOpen}
        title={
          pendingDelete?.type === 'gpt'
            ? 'GPT 요약 삭제'
            : '회고 삭제'
        }
        message={
          pendingDelete?.type === 'gpt'
            ? '이 날짜의 GPT 요약을 삭제하시겠습니까?'
            : '이 회고를 삭제하시겠습니까?'
        }
        loading={loading}
        onConfirm={handleConfirmDelete}
        onCancel={() => setConfirmOpen(false)}
      />

      {openMusicModal && selectedDiaryId !== null && (
        <MusicRecommendModal
          diaryEntryId={selectedDiaryId}
          onClose={() => {
            setOpenMusicModal(false);
            setSelectedDiaryId(null);
          }}
        />
      )}
    </div>
  );
};

export default DiaryListForDateModal;

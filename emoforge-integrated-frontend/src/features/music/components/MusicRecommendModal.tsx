import { useEffect, useState } from "react";
import { X } from "lucide-react";
import {
  getMusicRecommendations,
  requestMusicRecommendations,
} from "@/features/music/api/musicApi";
import { useToast } from "@/shared/stores/useToast";

interface MusicRecommendModalProps {
  diaryEntryId: number;
  onClose: () => void;
}

export const MusicRecommendModal = ({
  diaryEntryId,
  onClose,
}: MusicRecommendModalProps) => {
  const [history, setHistory] = useState<any[]>([]);
  const [artistInput, setArtistInput] = useState("");
  const [loading, setLoading] = useState(false);
  const toast = useToast();

  // ✅ 변경: useEffect 내부에서 응답 구조를 확인 후 songs만 추출
  useEffect(() => {
    let isMounted = true;

    (async () => {
      try {
        const res = await getMusicRecommendations(diaryEntryId);

        if (isMounted) {
          // ✅ 응답이 객체이고 songs 배열이 있으면 그것만 history로 세팅
          if (res && Array.isArray(res.songs) && res.songs.length > 0) {
            setHistory(res.songs);
          } else {
            console.warn("⚠️ 음악 추천 이력이 없습니다.");
          }
        }
      } catch (err) {
        if (isMounted) console.error("❌ 음악 추천 이력 조회 실패:", err);
      }
    })();

    return () => {
      isMounted = false;
    };
  }, [diaryEntryId]);

  // ✅ 변경: handleRecommend 함수 내 조건문 보강
  const handleRecommend = async () => {
    try {
      setLoading(true);
      const artistList = artistInput
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean);

      const res = await requestMusicRecommendations(diaryEntryId, artistList);

      // ✅ 추천된 노래가 없으면 DB 저장도 안 되고, 입력 화면 유지
      if (!res || !Array.isArray(res.songs) || res.songs.length === 0) {
        toast?.warn("추천 음악이 없습니다.");
        return; // ❌ setHistory() 호출하지 않음 → 입력 폼 그대로 유지
      }

      // ✅ 변경: setHistory 이후 링크 활성화 보장
      if (res && Array.isArray(res.songs) && res.songs.length > 0) {
        setHistory(res.songs);
        toast?.info("음악 추천 완료!");

        // ✅ 링크 클릭 안되는 문제 방지용 - 렌더 이후 pointer-events 복구
        setTimeout(() => {
          const links =
            document.querySelectorAll<HTMLAnchorElement>(".music-link");
          links.forEach((a) => (a.style.pointerEvents = "auto"));
        }, 100);
      }
    } catch (err) {
      toast?.error("추천 중 오류 발생!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-[100]">
      <div className="bg-white rounded-xl shadow-lg max-w-lg w-full p-6 relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-400 hover:text-black"
        >
          <X size={22} />
        </button>

        <h2 className="text-lg font-bold mb-3">🎵 음악 추천</h2>

        {/* ✅ 기존 추천 결과 */}
        {history.length > 0 ? (
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {history.map((item) => (
              <div
                key={item.id}
                className="flex items-center gap-3 p-2 border rounded"
              >
                {/* ✅ 썸네일 영역 추가 */}
                {item.thumbnailUrl ? (
                  <img
                    src={item.thumbnailUrl}
                    alt={`${item.songTitle} thumbnail`}
                    className="w-12 h-12 rounded object-cover flex-shrink-0"
                  />
                ) : (
                  <div className="w-12 h-12 bg-gray-200 rounded flex-shrink-0" />
                )}

                {/* ✅ 노래 정보 영역 */}
                <div className="flex flex-col">
                  <div className="font-semibold text-sm text-gray-800">
                    {item.title}
                  </div>
                  <div className="text-xs text-gray-600 mb-1">
                    {item.artist}
                  </div>
                  <a
                    href={item.youtubeUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="music-link text-blue-600 underline text-xs hover:text-blue-800"
                  >
                    ▶ YouTube 바로가기
                  </a>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <>
            <p className="text-sm text-gray-600 mb-2">
              선호 아티스트를 입력해주세요 (쉼표로 구분)
            </p>
            <input
              type="text"
              className="w-full border px-3 py-2 rounded mb-3 text-sm"
              placeholder="예: IU, Coldplay, BTS, Adele"
              value={artistInput}
              onChange={(e) => setArtistInput(e.target.value)}
            />
            <button
              onClick={handleRecommend}
              disabled={loading}
              className="w-full bg-blue-600 text-white rounded py-2 text-sm hover:bg-blue-700"
            >
              {loading ? "추천 중..." : "LangGraph로 추천받기"}
            </button>
          </>
        )}
      </div>
    </div>
  );
};

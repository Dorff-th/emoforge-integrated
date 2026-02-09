import { useState } from "react";
import { useNavigate } from "react-router-dom";
import EmotionSelector from "./EmotionSelector";
import HabitChecklist from "./HabitChecklist";
import FeelingInput from "./FeelingInput";
import DiaryTextarea from "./DiaryTextarea";
import FeedbackTypeSelect from "./FeedbackTypeSelect";
import SubmitButton from "./SubmitButton";
import GPTFeedbackModal from "@/features/gpt/components/GPTFeedbackModal";
import { type FeedbackType } from "@/features/gpt/types/feedbackTypes";
import { format } from "date-fns";
import { http } from "@/shared/api/httpClient";
import { langHttp } from "@/shared/api/langHttpClient";
import { useAuth } from "@/features/auth/hooks/useAuth";

//const DiaryForm: React.FC = () => {
const DiaryForm = () => {
  const [emotion, setEmotion] = useState<number>(0);
  const [habits, setHabits] = useState<string[]>([]);
  const [feelingText, setFeelingText] = useState("");
  const [feelingEnglish, setFeelingEnglish] = useState("");
  const [diary, setDiary] = useState("");
  const [feedbackType, setFeedbackType] = useState<FeedbackType>("random");

  const [showModal, setShowModal] = useState(false);
  const [gptMessage, setGptMessage] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const [isSaveSuccess, setIsSaveSuccess] = useState(false);

  const navigate = useNavigate();

  const { user } = useAuth();

  // ✅ LangGraph-Service 호출 (개선된 버전)
  const fetchGptFeedback = async (): Promise<string> => {
    setGptMessage("🤖 GPT 피드백 생성 중입니다...");
    setShowModal(true);

    // ⏱️ 타임아웃 처리
    const timeoutPromise = new Promise<string>((resolve) => {
      setTimeout(() => {
        console.warn("GPT 피드백 응답 지연으로 타임아웃 처리됨");
        resolve("응답이 지연되어 생략합니다.");
      }, 4000);
    });

    // ⚙️ 실제 LangGraph FastAPI 호출
    const requestPromise = langHttp
      .post("/diary/gpt/feedback", {
        emotionScore: emotion,
        habitTags: habits,
        feelingKo: feelingText,
        feelingEn: feelingEnglish,
        diaryContent: diary,
        feedbackStyle: feedbackType, // ✅ 이름 변경 주의
        uuid: user?.uuid,
      })
      .then((res) => {
        // LangGraph-Service에서 feedback 구조가 { summary, encouragement, next_tip }
        const feedback = res.data.feedback;
        if (typeof feedback === "object") {
          // 예쁘게 조합된 문자열로 표시 (모달에 보여줄 요약용)
          return `📝 ${feedback.summary || ""}\n\n💬 ${feedback.encouragement || ""}\n\n💡 ${feedback.next_tip || ""}`;
        } else {
          return (
            feedback || "오늘도 수고했어요. 내일은 더 잘할 수 있을 거예요!"
          );
        }
      })
      .catch((error) => {
        console.error("GPT 피드백 실패:", error);
        return "오늘도 수고했어요. 내일은 더 잘할 수 있을 거예요!";
      });

    return Promise.race([requestPromise, timeoutPromise]);
  };

  const handleSubmit = async () => {
    if (!emotion || !diary.trim()) {
      alert("감정과 회고 일기를 작성해주세요!");
      return;
    }

    setIsSaving(true);
    setIsSaveSuccess(false);

    try {
      // 1️⃣ LangGraph 기반 GPT 피드백 생성
      const gptFeedback = await fetchGptFeedback();
      setGptMessage(gptFeedback);

      // 2️⃣ Diary 저장
      const payload = {
        diaryDate: format(new Date(), "yyyy-MM-dd"),
        emotionScore: emotion,
        habitTags: habits,
        feelingKo: feelingText,
        feelingEn: feelingEnglish,
        content: diary,
        feedback: gptFeedback,
      };

      await http.post("/api/diary/diaries", payload);
      setIsSaveSuccess(true);
    } catch (e) {
      console.error("저장 중 오류 발생:", e);
      setGptMessage("저장에 실패했어요. 다시 시도해주세요. 😢");
      setIsSaveSuccess(false);
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto px-4 py-8 space-y-8">
      <h2 className="text-2xl font-bold mb-2">오늘의 감정 & 회고</h2>

      <EmotionSelector selected={emotion} onChange={setEmotion} />
      <HabitChecklist selectedHabits={habits} onChange={setHabits} />
      <FeelingInput
        value={feelingText}
        onChange={setFeelingText}
        selectedEnglish={feelingEnglish}
        onEnglishSelect={setFeelingEnglish}
      />
      <DiaryTextarea value={diary} onChange={setDiary} />
      <FeedbackTypeSelect value={feedbackType} onChange={setFeedbackType} />
      <SubmitButton isLoading={isSaving} onClick={handleSubmit} />

      {showModal && (
        <GPTFeedbackModal
          message={gptMessage}
          type={feedbackType}
          onClose={() => {
            setShowModal(false);
            if (isSaveSuccess) {
              navigate("/user/diary/calendar");
            }
          }}
          duration={6000}
        />
      )}
    </div>
  );
};

export default DiaryForm;

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import EmotionSelector from "./EmotionSelector";
import HabitChecklist from "./HabitChecklist";
import FeelingInput from "./FeelingInput";
import DiaryTextarea from "./DiaryTextarea";
import FeedbackTypeSelect from "./FeedbackTypeSelect";
import SubmitButton from "./SubmitButton";
//import GPTFeedbackModal from '@/features/gpt/components/GPTFeedbackModal';
//import { FeedbackType } from '@/features/gpt/types/feedbackTypes';
//import axiosDiary from "@/lib/axios/axiosDiary";
//import { format } from 'date-fns';
//import axiosLang from '@/lib/axios/axiosLang';

//const DiaryForm: React.FC = () => {
const DiaryForm = () => {
  const [emotion, setEmotion] = useState<number>(0);
  const [habits, setHabits] = useState<string[]>([]);
  const [feelingText, setFeelingText] = useState("");
  const [feelingEnglish, setFeelingEnglish] = useState("");
  const [diary, setDiary] = useState("");
  //const [feedbackType, setFeedbackType] = useState<FeedbackType>('random');

  const [showModal, setShowModal] = useState(false);
  const [gptMessage, setGptMessage] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const [isSaveSuccess, setIsSaveSuccess] = useState(false);

  const navigate = useNavigate();

  // ✅ LangGraph-Service 호출 (개선된 버전)

  const handleSubmit = async () => {};

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
      {/* <FeedbackTypeSelect value={feedbackType} onChange={setFeedbackType} /> */}
      <SubmitButton isLoading={isSaving} onClick={handleSubmit} />

      {/* {showModal && (
        <GPTFeedbackModal
          message={gptMessage}
          type={feedbackType}
          onClose={() => {
            setShowModal(false);
            if (isSaveSuccess) {
              navigate('/user/calendar');
            }
          }}
          duration={6000}
        />
      )} */}
    </div>
  );
};

export default DiaryForm;

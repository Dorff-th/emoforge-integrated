export type SlideGroup = {
  id: string;
  title: string;
  description: string;
  images: {
    src: string;
    alt: string;
  }[];
};

export const slideGroups: SlideGroup[] = [
  {
    id: "start",
    title: "간편한 시작과 최소 정보 가입",
    description: "이메일 없이, 필요한 정보만으로 서비스를 시작합니다.",
    images: [
      { src: "/images/01-1.auth_login.png", alt: "로그인 화면" },
      { src: "/images/01-2.agree.png", alt: "약관 동의 화면" },
    ],
  },
  {
    id: "diary",
    title: "오늘의 감정을 기록합니다",
    description: "감정 선택과 간단한 메모로 하루를 남길 수 있습니다.",
    images: [
      { src: "/images/03-1.diary_emotion_input.png", alt: "감정 입력 화면" },
    ],
  },
  {
    id: "ai",
    title: "기록을 정리해 주는 AI 피드백",
    description: "작성한 기록을 바탕으로 다양한 스타일의 피드백을 제공합니다.",
    images: [
      { src: "/images/03-2.GPT_feedback_style_select.png", alt: "GPT 피드백 선택" },
      { src: "/images/03-3.save_message.png", alt: "GPT 피드백 결과" },
    ],
  },
  {
    id: "history",
    title: "한눈에 보는 감정 기록 히스토리",
    description: "날짜별로 쌓인 기록을 캘린더에서 확인할 수 있습니다.",
    images: [
      { src: "/images/04-1.calendar.png", alt: "캘린더 화면" },
      { src: "/images/04-2.calendar_day_dialog.png", alt: "날짜별 기록 상세" },
    ],
  },
];

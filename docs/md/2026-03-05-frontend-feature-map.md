# Frontend Feature Map

## App Shell
관련 src 파일
- src/main.tsx
- src/App.css
- src/index.css
- src/app/App.tsx
- src/app/router.tsx
- src/app/providers/QueryProvider.tsx
- src/app/providers/useInitTheme.ts
- src/layouts/**
- src/guards/**
- src/components/ui/**
- src/shared/**
- src/lib/**

사용된 주요 라이브러리
- react
- react-dom
- react-router-dom
- @tanstack/react-query
- axios
- zustand
- @radix-ui/react-alert-dialog
- @radix-ui/react-tabs
- class-variance-authority
- clsx
- tailwind-merge
- lucide-react

feature 설명
- 전역 라우팅과 공통 UI 인프라를 제공한다.

## About
관련 src 파일
- src/features/about/**
- src/layouts/AboutLayout.tsx

사용된 주요 라이브러리
- react
- react-router-dom
- framer-motion
- lucide-react

feature 설명
- 서비스 소개와 포트폴리오 화면을 제공한다.

## Auth
관련 src 파일
- src/features/auth/**
- src/layouts/PublicLayout.tsx
- src/shared/api/httpClient.ts
- src/shared/api/endpoints.ts
- src/shared/stores/useAuthStore.ts
- src/shared/utils/redirectToKakaoLogin.ts

사용된 주요 라이브러리
- react
- react-router-dom
- @tanstack/react-query
- axios
- zustand

feature 설명
- 카카오 로그인과 약관 동의 흐름을 처리한다.

## User
관련 src 파일
- src/features/user/**
- src/layouts/UserLayout .tsx
- src/layouts/components/header/context/UserHeader.tsx

사용된 주요 라이브러리
- react
- axios
- zustand
- lucide-react
- @headlessui/react

feature 설명
- 사용자 홈과 프로필 관리 기능을 제공한다.

## Diary
관련 src 파일
- src/features/diary/**
- src/layouts/DiaryLayout.tsx
- src/layouts/components/header/context/DiaryHeader.tsx
- src/shared/types/diary.ts
- src/shared/constants/emotionMap.ts
- src/shared/utils/emotion.ts

사용된 주요 라이브러리
- react
- axios
- framer-motion
- @headlessui/react
- @mui/material
- @mui/x-date-pickers
- react-datepicker
- date-fns
- lucide-react

feature 설명
- 감정 일기 작성, 조회, 검색, 인사이트를 제공한다.

## Calendar
관련 src 파일
- src/features/calendar/**

사용된 주요 라이브러리
- react
- axios
- date-fns

feature 설명
- 월별 일기 기록을 캘린더로 탐색한다.

## Statistics
관련 src 파일
- src/features/statistics/**
- src/shared/utils/dateUtils.ts

사용된 주요 라이브러리
- react
- recharts
- date-fns

feature 설명
- 감정과 활동 데이터를 차트로 시각화한다.

## Post
관련 src 파일
- src/features/post/**
- src/layouts/PostLayout.tsx
- src/layouts/components/header/context/PostHeader.tsx
- src/shared/components/NewPostButton.tsx
- src/shared/utils/contentUrlHelper.ts
- src/shared/utils/resolveAttachmentUrl.ts

사용된 주요 라이브러리
- react
- react-router-dom
- axios
- @toast-ui/react-editor
- lucide-react

feature 설명
- 게시글 작성, 조회, 검색, 댓글 기능을 제공한다.

## Admin
관련 src 파일
- src/features/admin/**
- src/layouts/AdminLayout.tsx
- src/layouts/AdminProtectedLayout.tsx
- src/layouts/AdminHeader.tsx
- src/layouts/AdminSidebar.tsx
- src/layouts/components/header/context/AdminHeader.tsx
- src/guards/RequireAdmin.tsx

사용된 주요 라이브러리
- react
- react-router-dom
- @tanstack/react-query
- axios
- react-google-recaptcha
- @toast-ui/react-editor
- class-variance-authority
- lucide-react

feature 설명
- 관리자 인증과 콘텐츠 운영 기능을 제공한다.

## GPT Feedback
관련 src 파일
- src/features/gpt/**
- src/features/diary/ui/GptFeelingLoadingModal.tsx

사용된 주요 라이브러리
- react
- axios

feature 설명
- 일기 기반 AI 피드백 조회를 담당한다.

## Music Recommendation
관련 src 파일
- src/features/music/**

사용된 주요 라이브러리
- react
- axios
- lucide-react

feature 설명
- 감정 기반 음악 추천 모달을 제공한다.

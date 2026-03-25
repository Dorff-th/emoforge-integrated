src/App.css // app styles
src/app/App.tsx // root app component
src/app/providers/QueryProvider.tsx // react query provider
src/app/providers/useInitTheme.ts // initial theme hook
src/app/router.tsx // application route definitions
src/assets/characters/loading_bunny_gpt.png // loading bunny image
src/assets/react.svg // React logo asset
src/assets/terms/privacy_policy.txt // privacy policy text
src/assets/terms/service_terms.txt // service terms text
src/assets/toast/toast_char_1.png // toast char 1 image asset
src/assets/toast/toast_char_2.png // toast char 2 image asset
src/assets/toast/toast_char_3.png // toast char 3 image asset
src/assets/toast/toast_char_4.png // toast char 4 image asset
src/assets/toast/toast_char_5.png // toast char 5 image asset
src/assets/toast/toast_char_6.png // toast char 6 image asset
src/components/ui/alert-dialog.tsx // alert dialog primitive
src/components/ui/button.tsx // button primitive
src/components/ui/card.tsx // card primitive
src/components/ui/tabs.tsx // tabs primitive
src/features/about/components/IntroSection/ArchitectureSection.tsx // architecture section component
src/features/about/components/IntroSection/BoardSection.tsx // board section component
src/features/about/components/IntroSection/ClosingSection.tsx // closing section component
src/features/about/components/IntroSection/FeatureCard.tsx // feature card component
src/features/about/components/IntroSection/FeatureSection.tsx // feature section component
src/features/about/components/IntroSection/HeroSection.tsx // hero section component
src/features/about/components/portfolio/ArchitectureToggleSection.tsx // architecture toggle section component
src/features/about/pages/AboutIntroPage.tsx // about intro page
src/features/about/pages/PortfolioPage.tsx // portfolio page
src/features/admin/api/admin.type.ts // admin type definitions
src/features/admin/api/adminApi.ts // admin auth API
src/features/admin/api/adminCategoryApi.ts // admin category API client
src/features/admin/api/adminMemberApi.ts // admin member API client
src/features/admin/api/adminPostApi.ts // admin post API client
src/features/admin/api/dashboardApi.ts // dashboard API client
src/features/admin/components/AdminAuthLoading.tsx // admin auth loading component
src/features/admin/components/member/DeleteTogglePill.tsx // delete toggle pill component
src/features/admin/components/member/MemberDeleteButton.tsx // member delete button component
src/features/admin/components/member/MemberSearchBar.tsx // member search bar component
src/features/admin/components/posts/AdminCommentSearchBar.tsx // admin comment search bar component
src/features/admin/components/posts/AdminPostSearchBar.tsx // admin post search bar component
src/features/admin/components/ui/StatusPill.tsx // status pill UI component
src/features/admin/hooks/useAdminAuth.ts // admin auth hook
src/features/admin/hooks/useAdminLogout.ts // admin logout hook
src/features/admin/hooks/useAdminMe.ts // admin me hook
src/features/admin/hooks/useCategories.ts // categories hook
src/features/admin/hooks/useDashboard.ts // dashboard hook
src/features/admin/hooks/useMembers.ts // members hook
src/features/admin/pages/_AdminDashboardPage copy.tsx_ // admin dashboard page copy
src/features/admin/pages/AdminCommentsPage.tsx // admin comments page
src/features/admin/pages/AdminDashboardPage.tsx // admin dashboard page
src/features/admin/pages/AdminLoginPage.tsx // admin login page
src/features/admin/pages/AdminMembersPage.tsx // admin members page
src/features/admin/pages/AdminPostCategoryPage.tsx // admin post category page
src/features/admin/pages/AdminPostDetailPage.tsx // admin post detail page
src/features/admin/pages/AdminPostEditPage.tsx // admin post edit page
src/features/admin/pages/AdminPostListPage.tsx // admin post list page
src/features/auth/api/auth.types.ts // auth type definitions
src/features/auth/api/authApi.ts // auth API client
src/features/auth/api/authFlow.ts // auth flow tracker
src/features/auth/hooks/useAuth.ts // auth hook
src/features/auth/hooks/useKakaoAuth.ts // kakao auth hook
src/features/auth/hooks/useTermsModalStore.ts // terms modal store hook
src/features/auth/pages/LoginPage.tsx // login page
src/features/auth/pages/OAuthCallbackPage.tsx // oauth callback page
src/features/auth/pages/TermsAgreementPage.tsx // terms agreement page
src/features/calendar/api/calendarApi.ts // calendar API client
src/features/calendar/components/Calendar.tsx // calendar component
src/features/calendar/components/CalendarDayCell.tsx // calendar day cell component
src/features/calendar/components/CalendarSelector.tsx // calendar selector component
src/features/calendar/hooks/useCalendarData.tsx // calendar data hook
src/features/calendar/pages/CalendarPage.tsx // calendar page
src/features/calendar/types/calendar.types.ts // calendar type definitions
src/features/diary/api/diaryApi.ts // diary API client
src/features/diary/components/DiaryDetail.tsx // diary detail component
src/features/diary/components/DiaryForm.tsx // diary form component
src/features/diary/components/DiaryItem.tsx // diary item component
src/features/diary/components/DiaryListForDateModal.tsx // diary list for date modal component
src/features/diary/components/DiarySearchInput.tsx // diary search input component
src/features/diary/components/DiaryTextarea.tsx // diary textarea component
src/features/diary/components/EmotionSelector.tsx // emotion selector component
src/features/diary/components/FeedbackTypeSelect.tsx // feedback type select component
src/features/diary/components/FeelingInput.tsx // feeling input component
src/features/diary/components/HabitChecklist.tsx // habit checklist component
src/features/diary/components/Pagination.tsx // pagination component
src/features/diary/components/SubmitButton.tsx // submit button component
src/features/diary/pages/DiaryInsightsPage.tsx // diary insights page
src/features/diary/pages/DiaryListPage.tsx // diary list page
src/features/diary/pages/DiarySearchPage.tsx // diary search page
src/features/diary/pages/DiaryWritePage.tsx // diary write page
src/features/diary/search/diarySearch.types.ts // diary search type definitions
src/features/diary/search/diarySearchParams.ts // diary search params helper
src/features/diary/search/useDiarySearch.ts // diary search hook
src/features/diary/ui/EmotionRangeSlider.tsx // emotion range slider component
src/features/diary/ui/GptFeelingLoadingModal.tsx // GPT feeling loading modal
src/features/gpt/api/gptSummaryApi.ts // GPT summary API
src/features/gpt/components/GPTFeedbackModal.tsx // GPT feedback modal component
src/features/gpt/types/feedbackTypes.ts // feedback type definitions
src/features/music/api/musicApi.ts // music recommendation API
src/features/music/components/MusicRecommendModal.tsx // music recommend modal component
src/features/post/api/categoryApi.ts // category API client
src/features/post/api/postApi.ts // post API client
src/features/post/api/tagApi.ts // tag API client
src/features/post/components/AttachmentUploader.tsx // attachment uploader component
src/features/post/components/CategoryFilter.tsx // category filter component
src/features/post/components/CategorySelect.tsx // category select component
src/features/post/components/FormActions.tsx // form actions component
src/features/post/components/Pagination.tsx // pagination component
src/features/post/components/PostContentEditor.tsx // post content editor component
src/features/post/components/PostForm.tsx // post form component
src/features/post/components/PostNavigation/PostNavigation.tsx // post navigation component
src/features/post/components/PostTagInput.tsx // post tag input component
src/features/post/components/PostTitleInput.tsx // post title input component
src/features/post/components/UserComment.tsx // user comment component
src/features/post/pages/PostDetailPage.tsx // post detail page
src/features/post/pages/PostEditPage.tsx // post edit page
src/features/post/pages/PostListPage.tsx // post list page
src/features/post/pages/PostSearchPage.tsx // post search page
src/features/post/pages/PostWritePage.tsx // post write page
src/features/post/pages/TagPostListPage.tsx // tag post list page
src/features/post/types/Attachment.ts // attachment type definitions
src/features/post/types/Category.ts // category type definitions
src/features/post/types/Comment.ts // comment type definitions
src/features/post/types/Common.ts // common type definitions
src/features/post/types/Post.tsx // post type definitions
src/features/post/types/PostRequest.ts // post request type definitions
src/features/post/types/Tag.ts // tag type definitions
src/features/statistics/api/statisticsApi.ts // statistics API client
src/features/statistics/components/DayOfWeekBarChart.tsx // day of week bar chart component
src/features/statistics/components/EmotionBarChart.tsx // emotion bar chart component
src/features/statistics/components/EmotionSummaryCard.tsx // emotion summary card component
src/features/statistics/components/WeeklyLineChart.tsx // weekly line chart component
src/features/statistics/types/statisticsTypes.ts // statistics type definitions
src/features/user/api/profile.api.ts // profile API client
src/features/user/api/profileImageApi.ts // profile image API
src/features/user/api/userStatApi.ts // user stats API
src/features/user/api/withdrawalApi.ts // withdrawal API client
src/features/user/components/profile/modals/EmailEditModal.tsx // email edit modal component
src/features/user/components/profile/modals/NicknameEditModal.tsx // nickname edit modal component
src/features/user/components/profile/modals/ProfileImageUploadModal.tsx // profile image upload modal component
src/features/user/components/profile/ProfileHeader.tsx // profile header component
src/features/user/components/profile/ProfileImageSection.tsx // profile image section component
src/features/user/components/profile/ProfileInfoSection.tsx // profile info section component
src/features/user/components/profile/ProfileStatsSection.tsx // profile stats section component
src/features/user/components/profile/ProfileWithdrawalSection.tsx // profile withdrawal section component
src/features/user/components/userHome/DiaryEntryCard.tsx // diary entry card component
src/features/user/components/userHome/DiaryQuickActions.tsx // diary quick actions component
src/features/user/components/userHome/EmotionEmojiDisplay.tsx // emotion emoji display component
src/features/user/components/userHome/NoTodayDiaryState.tsx // no today diary state component
src/features/user/components/userHome/TodayDiarySection.tsx // today diary section component
src/features/user/components/userHome/TodayEmotionState.tsx // today emotion state component
src/features/user/components/userHome/TodayHeroSection.tsx // today hero section component
src/features/user/components/userHome/TodayPostActivitySection.tsx // today post activity section component
src/features/user/components/userHome/TodaySummarySection.tsx // today summary section component
src/features/user/hooks/useLogout.ts // logout hook
src/features/user/hooks/useProfileImage.ts // profile image hook
src/features/user/hooks/useTodayHomeData.ts // today home data hook
src/features/user/hooks/useWithdrawal.ts // withdrawal hook
src/features/user/pages/ProfilePage.tsx // profile page
src/features/user/pages/UserHomePage.tsx // user home page
src/features/user/pages/WithdrawalPendingPage.tsx // withdrawal pending page
src/guards/RequireAdmin.tsx // admin route guard
src/guards/RequireAuth.tsx // authenticated route guard
src/index.css // global styles
src/layouts/AboutLayout.tsx // about section layout
src/layouts/AdminHeader.tsx // admin header layout
src/layouts/AdminLayout.tsx // admin area layout
src/layouts/AdminProtectedLayout.tsx // protected admin layout
src/layouts/AdminSidebar.tsx // admin sidebar component
src/layouts/components/AuthErrorHandler.tsx // auth error handler
src/layouts/components/header/AppHeader.tsx // application header
src/layouts/components/header/AppHeaderRight.tsx // header right section
src/layouts/components/header/context/AdminHeader.tsx // admin header component
src/layouts/components/header/context/DiaryHeader.tsx // diary header component
src/layouts/components/header/context/headerContext.tsx // header context provider
src/layouts/components/header/context/PostHeader.tsx // post header component
src/layouts/components/header/context/PublicHeader.tsx // public header component
src/layouts/components/header/context/UserHeader.tsx // user header component
src/layouts/components/header/elements/HeaderNavItem.tsx // header nav item component
src/layouts/components/header/elements/LoginButton.tsx // login button component
src/layouts/components/header/elements/Logo.tsx // logo component
src/layouts/components/header/elements/PostSearchInput.tsx // post search input component
src/layouts/components/header/elements/SearchInputUI.tsx // search input UI component
src/layouts/components/header/elements/ThemeToggle.tsx // theme toggle component
src/layouts/components/header/header.constants.ts // header constants
src/layouts/components/header/profileMenu/ProfileDropdown.tsx // profile dropdown component
src/layouts/components/header/profileMenu/ProfileMenu.tsx // profile menu component
src/layouts/components/header/profileMenu/ProfileMenuItem.tsx // profile menu item component
src/layouts/components/header/profileMenu/ProfileSummary.tsx // profile summary component
src/layouts/components/header/profileMenu/ProfileTrigger.tsx // profile trigger component
src/layouts/components/hooks/useHeaderContext.ts // header context hook
src/layouts/components/hooks/useHeaderScrolled.ts // header scroll hook
src/layouts/components/hooks/useOnClickOutside.ts // outside click hook
src/layouts/DiaryLayout.tsx // diary section layout
src/layouts/PostLayout.tsx // post section layout
src/layouts/PublicLayout.tsx // public route layout
src/layouts/UserLayout .tsx // user area layout
src/lib/queryClient.ts // react query client
src/lib/utils.ts // shared utility helpers
src/main.tsx // app entry point
src/shared/api/endpoints.ts // API endpoint constants
src/shared/api/httpClient.ts // axios HTTP client
src/shared/api/langHttpClient.ts // language API client
src/shared/components/Avatar.tsx // avatar component
src/shared/components/ConfirmDialog.tsx // confirm dialog component
src/shared/components/ConfirmModal.tsx // confirm modal component
src/shared/components/GlobalLoadingOverlay.tsx // global loading overlay component
src/shared/components/Modal.tsx // modal component
src/shared/components/NewPostButton.tsx // new post button component
src/shared/components/SectionLoading.tsx // section loading component
src/shared/components/ToastHost.tsx // toast host component
src/shared/components/ToastItem.tsx // toast item component
src/shared/config/config.ts // frontend config values
src/shared/constants/emotionMap.ts // emotion mapping constants
src/shared/hooks/useDebounce.ts // debounce hook
src/shared/stores/loadingStore.ts // loading state store
src/shared/stores/themeStore.ts // theme state store
src/shared/stores/toast.types.ts // toast type definitions
src/shared/stores/toastStore.ts // toast state store
src/shared/stores/useAuthStore.ts // auth state store
src/shared/stores/useToast.ts // toast hook store
src/shared/stores/useUILoading.ts // UI loading hook store
src/shared/types/diary.ts // diary type definitions
src/shared/utils/calendarUtils.ts // calendar utility helpers
src/shared/utils/cn.ts // class name merger
src/shared/utils/contentUrlHelper.ts // content URL helpers
src/shared/utils/dateUtils.ts // date utility helpers
src/shared/utils/emotion.ts // emotion utility helpers
src/shared/utils/fileUtils copy.ts // file helper copy
src/shared/utils/fileUtils.ts // file utility helpers
src/shared/utils/generateId.ts // ID generator helper
src/shared/utils/redirectToKakaoLogin.ts // kakao login redirect helper
src/shared/utils/resolveAttachmentUrl.ts // attachment URL resolver

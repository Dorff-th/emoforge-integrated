Feature: about

Page: AboutIntroPage
api
- none

Page: PortfolioPage
api
- none

---

Feature: admin

Page: AdminCommentsPage
api
- bulkDeleteAdminComments
- fetchAdminComments

Page: AdminDashboardPage
api
- fetchDashboardStats

Page: AdminLoginPage
api
- adminApi

Page: AdminMembersPage
api
- axios
- deleteMember
- http

Page: AdminPostCategoryPage
api
- createCategory
- deleteCategory
- fetchCategories
- updateCategory

Page: AdminPostDetailPage
api
- deleteAdminPost
- fetchAdminPostDetail
- getPostTags

Page: AdminPostEditPage
api
- authApi
- axios
- fetchCategories
- fetchPostDetail
- fetchSuggest
- getPostTags
- http

Page: AdminPostListPage
api
- bulkDeleteAdminPosts
- fetchAdminPosts

---

Feature: auth

Page: LoginPage
api
- none

Page: OAuthCallbackPage
api
- authApi

Page: TermsAgreementPage
api
- authApi
- fetch

---

Feature: calendar

Page: CalendarPage
api
- deleteDiaryEntry
- deleteGptSummaryOnly
- fetchMonthDiaryList
- generateGptSummary
- getMusicRecommendations
- requestMusicRecommendations

---

Feature: diary

Page: DiaryInsightsPage
api
- fetchEmotionStatistics

Page: DiaryListPage
api
- fetchDiaryList

Page: DiarySearchPage
api
- axios
- http

Page: DiaryWritePage
api
- authApi
- axios
- http

---

Feature: post

Page: PostDetailPage
api
- authApi
- createComment
- deleteComment
- deletePost
- fetchCommentsByPostId
- fetchPostDetail
- fetchPosts
- fetchProfileImage
- getPostTags
- uploadProfileImage

Page: PostEditPage
api
- authApi
- axios
- fetchCategories
- fetchPostDetail
- fetchSuggest
- getPostTags
- http

Page: PostListPage
api
- authApi
- fetchCategories
- fetchPosts

Page: PostSearchPage
api
- axios
- http

Page: PostWritePage
api
- authApi
- axios
- fetchCategories
- fetchSuggest
- getPostTags
- http

Page: TagPostListPage
api
- authApi
- fetchPostsByTag

---

Feature: user

Page: ProfilePage
api
- authApi
- checkEmail
- checkNickname
- fetchMemberAttachmentStats
- fetchMemberDiaryStats
- fetchMemberPostStats
- fetchProfileImage
- requestWithdrawal
- requestWithdrawalCancle
- updateEmail
- updateNickname
- uploadProfileImage

Page: UserHomePage
api
- fetchMemberPostStatsToday
- fetchTodayDiary

Page: WithdrawalPendingPage
api
- authApi
- requestWithdrawal
- requestWithdrawalCancle


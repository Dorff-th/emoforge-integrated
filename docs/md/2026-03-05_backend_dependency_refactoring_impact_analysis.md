# Backend Dependency Refactoring 영향 분석 (2026-03-05)

## 분석 대상 변경
1. domain module에서 spring-boot-starter-security 중복 제거
2. org.springdoc:springdoc-openapi-starter-webmvc-ui를 emoforge-core -> emoforge-app 이동
3. jakarta.servlet:jakarta.servlet-api 제거

## 1) 영향 가능 파일 전체 목록

### A. 의존성 선언/이동에 직접 영향 있는 Gradle 파일
- .\emoforge-core\build.gradle:7:    api "jakarta.servlet:jakarta.servlet-api"
- .\emoforge-core\build.gradle:10:    api "org.springframework.boot:spring-boot-starter-security"
- .\emoforge-core\build.gradle:18:    api "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0"
- .\module-post\build.gradle:12:    implementation "org.springframework.boot:spring-boot-starter-security"
- .\module-diary\build.gradle:9:    implementation "org.springframework.boot:spring-boot-starter-security"
- .\module-attachment\build.gradle:12:    implementation "org.springframework.boot:spring-boot-starter-security"
- .\module-auth\build.gradle:9:    //implementation "org.springframework.boot:spring-boot-starter-security" -> emoforge-core모듈로 이동

### B. springdoc 이동 시 컴파일 영향 가능 Java 파일(io.swagger import 사용)
- 총 92개 파일
- 모듈별: module-attachment=11, module-auth=24, module-diary=25, module-post=32
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachHealthController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachmentCleanupController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachmentController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachmentStatsController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachTestController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\PublicAttachController.java
- module-attachment/src/main/java\dev\emoforge\attach\dto\AttachmentConfirmRequest.java
- module-attachment/src/main/java\dev\emoforge\attach\dto\AttachmentDeleteRequest.java
- module-attachment/src/main/java\dev\emoforge\attach\dto\AttachmentResponse.java
- module-attachment/src/main/java\dev\emoforge\attach\dto\MemberAttachmentStatsResponse.java
- module-attachment/src/main/java\dev\emoforge\attach\service\AttachmentCleanupService.java
- module-auth/src/main/java\dev\emoforge\auth\controller\admin\AdminAuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\admin\MemberAdminController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\AuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\AuthHealthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\KakaoAuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\KakaoSignupController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\MemberProfileController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\MemberWithdrawalController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\PublicProfileController.java
- module-auth/src/main/java\dev\emoforge\auth\dto\admin\AdminLoginRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\admin\AdminLoginResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\AvailabilityResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\KakaoSignupRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\KakaoSignupResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\LoginRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\LoginResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\MemberDTO.java
- module-auth/src/main/java\dev\emoforge\auth\dto\MemberProfileResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\PublicProfileResponse.java
- module-auth/src/main/java\dev\emoforge\auth\dto\SignUpRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\UpdateEmailRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\UpdateNicknameRequest.java
- module-auth/src/main/java\dev\emoforge\auth\infra\kakao\KakaoCodeRequest.java
- module-auth/src/main/java\dev\emoforge\auth\infra\kakao\KakaoLoginResult.java
- module-diary/src/main/java\dev\emoforge\diary\controller\CustomErrorController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryActivityStatsController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryEntryController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryHealthController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiarySearchController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryWelcomeController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\EmotionStatisticsController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\MusicRecommendController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\SummaryController.java
- module-diary/src/main/java\dev\emoforge\diary\dto\music\MusicRecommendHistoryDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\music\MusicRecommendRequest.java
- module-diary/src/main/java\dev\emoforge\diary\dto\music\MusicRecommendSongDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\music\RecommendResultDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\request\DiarySaveRequestDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\request\DiarySearchRequestDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\request\GPTSummaryRequestDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\DiaryEntryDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\DiaryGroupPageResponseDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\DiaryGroupResponseDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\DiarySearchResultDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\GPTSummaryResponseDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\MemberDiaryStatsResponse.java
- module-diary/src/main/java\dev\emoforge\diary\dto\response\SummaryResponseDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\statistics\EmotionStatisticsDTO.java
- module-diary/src/main/java\dev\emoforge\diary\dto\statistics\WeeklyTrendDTO.java
- module-post/src/main/java\dev\emoforge\post\admin\controller\AdminCategoryController.java
- module-post/src/main/java\dev\emoforge\post\admin\controller\AdminTestController.java
- module-post/src/main/java\dev\emoforge\post\admin\dto\bff\AdminPageResponseDTO.java
- module-post/src/main/java\dev\emoforge\post\admin\dto\bff\AdminPostListItemDTO.java
- module-post/src/main/java\dev\emoforge\post\controller\CategoryApiController.java
- module-post/src/main/java\dev\emoforge\post\controller\CommentController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostHealthController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostStatisticsController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostTestController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostWelcomeController.java
- module-post/src/main/java\dev\emoforge\post\controller\TagController.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\CategoryRequest.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\CategoryUpdateRequest.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\CommentRequest.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\CommentResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PageRequestDTO.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PostDetailResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PostRequestDTO.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PostSearchFilter.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PostStatsResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\PostUpdateDTO.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\SearchRequestWrapper.java
- module-post/src/main/java\dev\emoforge\post\dto\internal\TagResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\legacy\bff\CommentDetailResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\legacy\bff\MatchedField.java
- module-post/src/main/java\dev\emoforge\post\dto\legacy\bff\PageResponseDTO.java
- module-post/src/main/java\dev\emoforge\post\dto\legacy\bff\PostListItemResponse.java
- module-post/src/main/java\dev\emoforge\post\dto\legacy\bff\SearchResultDTO.java
- module-post/src/main/java\dev\emoforge\post\dto\query\PostListItemProjection.java
- module-post/src/main/java\dev\emoforge\post\dto\query\PostListItemSummary.java
- module-post/src/main/java\dev\emoforge\post\dto\query\PostSearchResultDTO.java

### C. jakarta.servlet-api 제거 시 영향 가능 Java 파일(jakarta.servlet import 사용)
- 총 11개 파일
- emoforge-core/src/main/java\dev\emoforge\core\security\handler\JwtAccessDeniedHandler.java
- emoforge-core/src/main/java\dev\emoforge\core\security\handler\JwtAuthenticationEntryPoint.java
- emoforge-core/src/main/java\dev\emoforge\core\security\jwt\JwtAuthenticationFilter.java
- module-auth/src/main/java\dev\emoforge\auth\controller\admin\AdminAuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\AuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\KakaoAuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\KakaoSignupController.java
- module-auth/src/main/java\dev\emoforge\auth\service\KakaoAuthService.java
- module-auth/src/main/java\dev\emoforge\auth\service\LoginTokenService.java
- module-diary/src/main/java\dev\emoforge\diary\controller\CustomErrorController.java
- module-post/src/main/java\dev\emoforge\post\config\FeignAuthInterceptor.java

### D. Security import/설정 영향 가능 Java 파일
- 총 31개 파일
- emoforge-app/src/main/java\dev\emoforge\app\security\SecurityConfig.java
- emoforge-core/src/main/java\dev\emoforge\core\security\handler\JwtAccessDeniedHandler.java
- emoforge-core/src/main/java\dev\emoforge\core\security\handler\JwtAuthenticationEntryPoint.java
- emoforge-core/src/main/java\dev\emoforge\core\security\jwt\JwtAuthenticationFilter.java
- emoforge-core/src/main/java\dev\emoforge\core\security\principal\CustomUserPrincipal.java
- emoforge-core/src/main/java\dev\emoforge\core\security\util\SecurityUtils.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachmentStatsController.java
- module-attachment/src/main/java\dev\emoforge\attach\controller\AttachTestController.java
- module-auth/src/main/java\dev\emoforge\auth\config\PasswordConfig.java
- module-auth/src/main/java\dev\emoforge\auth\controller\admin\AdminAuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\admin\MemberAdminController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\AuthController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\MemberProfileController.java
- module-auth/src/main/java\dev\emoforge\auth\controller\MemberWithdrawalController.java
- module-auth/src/main/java\dev\emoforge\auth\security\oauth\CustomOAuth2User.java
- module-auth/src/main/java\dev\emoforge\auth\service\admin\AdminAuthService.java
- module-auth/src/main/java\dev\emoforge\auth\service\CustomOAuth2UserService.java
- module-auth/src/main/java\dev\emoforge\auth\service\KakaoSignupService.java
- module-auth/src/main/java\dev\emoforge\auth\service\MemberService.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryActivityStatsController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryEntryController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiarySearchController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\DiaryWelcomeController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\EmotionStatisticsController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\MusicRecommendController.java
- module-diary/src/main/java\dev\emoforge\diary\controller\SummaryController.java
- module-post/src/main/java\dev\emoforge\post\admin\controller\AdminTestController.java
- module-post/src/main/java\dev\emoforge\post\controller\CommentController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostStatisticsController.java
- module-post/src/main/java\dev\emoforge\post\controller\PostTestController.java

## 2) 변경이 필요한 build.gradle 파일 목록
- emoforge-core/build.gradle
  - 제거 대상: jakarta.servlet:jakarta.servlet-api
  - 이동 대상: org.springdoc:springdoc-openapi-starter-webmvc-ui (core에서 제거)
- emoforge-app/build.gradle
  - 추가 대상: org.springdoc:springdoc-openapi-starter-webmvc-ui
- module-post/build.gradle
  - 제거 대상: spring-boot-starter-security
- module-diary/build.gradle
  - 제거 대상: spring-boot-starter-security
- module-attachment/build.gradle
  - 제거 대상: spring-boot-starter-security

참고: module-auth/build.gradle는 이미 security starter가 주석 처리되어 있어 직접 변경 필요성은 낮음.

## 3) Security import/bean 설정 영향 확인 결과
영향 가능 파일이 존재함. 특히 아래 파일은 설정/빈 관점에서 우선 확인 필요:
- emoforge-app/src/main/java\dev\emoforge\app\security\SecurityConfig.java
- module-auth/src/main/java\dev\emoforge\auth\config\PasswordConfig.java
- module-auth/src/main/java\dev\emoforge\auth\dto\admin\AdminLoginRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\LoginRequest.java
- module-auth/src/main/java\dev\emoforge\auth\dto\SignUpRequest.java
- module-auth/src/main/java\dev\emoforge\auth\service\admin\AdminAuthService.java
- module-auth/src/main/java\dev\emoforge\auth\service\KakaoSignupService.java
- module-auth/src/main/java\dev\emoforge\auth\service\MemberService.java

점검 결론
- 현재 구조에서는 emoforge-core가 spring-boot-starter-security를 api로 노출하므로, domain 모듈에서 starter를 제거해도 보안 import 자체는 대체로 유지될 가능성이 높음.
- 다만 모듈 단독 빌드/실행 방식, 또는 향후 core의 api -> implementation 변경 시 즉시 깨질 수 있으므로 의존 정책을 명확히 해야 함.

## 4) 변경별 예상 리스크
### 변경 1) domain module security starter 중복 제거
- 리스크: 모듈 단독 실행/테스트 시 보안 자동설정 클래스패스 차이로 동작이 달라질 수 있음.
- 리스크: module-post, module-diary, module-attachment가 core의 api 노출에 더 강하게 결합됨.
- 확인 포인트: 각 모듈의 테스트/부트스트랩이 app 조합 없이도 동작하는지 여부.

### 변경 2) springdoc를 core -> app 이동
- 고위험: 현재 다수의 domain Java 파일이 io.swagger.v3 import를 사용 중이며, core에서 springdoc 제거 시 domain 컴파일이 깨질 가능성이 큼.
- 리스크: OpenAPI 어노테이션 타입(@Operation, @Schema, @Tag, @Hidden) 미해결로 컴파일 실패.
- 완화 방안:
  - (a) 각 domain 모듈에 compileOnly io.swagger.core.v3:swagger-annotations 추가, 또는
  - (b) 공통 문서화 어노테이션 의존을 별도 shared-doc 모듈로 분리.

### 변경 3) jakarta.servlet-api 제거
- 중간 리스크: servlet 타입을 직접 import한 파일이 존재(총 11개)하므로 클래스패스에서 해당 타입이 계속 공급되어야 함.
- 현재는 spring-web/spring-security-web 전이 의존으로 대체될 가능성이 높지만, 의존 버전/스코프 변경 시 회귀 위험 존재.
- 확인 포인트: JwtAuthenticationFilter, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler, auth controller 계열 컴파일/런타임 동작.

## 요약
- 실제 수정 타깃 Gradle 파일은 5개(core, app, post, diary, attachment).
- 가장 큰 위험은 springdoc 이동으로 인한 io.swagger.v3 import 컴파일 깨짐이며, 영향 파일 수가 많아 선행 대응이 필요.
- Security starter 중복 제거와 servlet-api 제거는 상대적으로 저위험이지만, 모듈 단독 실행/테스트 시나리오 재검증이 필요.

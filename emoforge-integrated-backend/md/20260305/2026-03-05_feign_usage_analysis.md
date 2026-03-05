# OpenFeign 사용 여부 및 MSA 잔재 분석 (2026-03-05)

## 범위
- spring-cloud-starter-openfeign 의존성
- @FeignClient 사용 코드
- Feign 설정/인터셉터
- Feign DTO/legacy client 코드
- Monolith 내부 호출 대체 가능성

## 결론 요약
- 현재 코드베이스에는 Feign 관련 코드가 남아 있으나, 실제 운영 경로는 대부분 내부 Query/Repository 기반으로 대체된 상태다.
- `@FeignClient`는 3곳 존재하지만, `@EnableFeignClients`가 보이지 않고(`EmoforgeApplication` 기준), `@Primary` Dummy 구현체가 존재해 실질 원격 호출 경로는 레거시/비활성 추정이 강하다.
- OpenFeign 제거 시 컴파일 직접 영향 파일은 제한적(Feign import 파일 + build.gradle)이며, 기능 영향은 auth의 public profile 이미지 조회 경로를 어떻게 내부화하느냐에 달려 있다.

---

## 1) 프로젝트 전체 @FeignClient 사용 위치

1. module-auth/src/main/java/dev/emoforge/auth/service/external/AttachClient.java:8
- `@FeignClient(name = "attach-service", url = "${service.attach.url}")`

2. module-post/src/main/java/dev/emoforge/post/service/legacy/external/AttachClient.java:11
- `@FeignClient(name = "attach-service", url = "${service.attach.url}")`

3. module-post/src/main/java/dev/emoforge/post/service/legacy/external/AuthClient.java:7
- `@FeignClient(name = "auth-service", url = "${service.auth.url}")`

참고:
- `@EnableFeignClients` 선언은 검색되지 않음.
- emoforge-app/src/main/java/dev/emoforge/app/EmoforgeApplication.java 에서도 확인되지 않음.

---

## 2) FeignClient 대상 서비스 정리

1. auth 모듈 AttachClient
- 대상: `attach-service`
- URL 프로퍼티: `${service.attach.url}`
- 호출 API: `GET /api/attach/public/profile`

2. post 모듈 AttachClient (legacy)
- 대상: `attach-service`
- URL 프로퍼티: `${service.attach.url}`
- 호출 API:
  - `POST /api/attach/count/batch`
  - `GET /api/attach/post/{postId}`
  - `GET /api/attach/profile-images/{memberUuid}`
  - `DELETE /api/attach/post/{postId}`

3. post 모듈 AuthClient (legacy)
- 대상: `auth-service`
- URL 프로퍼티: `${service.auth.url}`
- 호출 API:
  - `GET /api/auth/public/members/{uuid}/profile`

설정 관찰:
- `service.attach.url`은 application-*.yml에 존재
  - emoforge-app/src/main/resources/application-dev.yml:83-85
  - emoforge-app/src/main/resources/application-local-docker.yml:89-91
  - emoforge-app/src/main/resources/application-prod.yml:86-88
- `service.auth.url`은 설정 파일에서 확인되지 않음(Feign 활성화 시 잠재 리스크)

---

## 3) Monolith 내부 Service 호출 대체 가능성 분석

### A. auth -> attach (public profile 이미지)
현재:
- `MemberPublicProfileService`가 `AttachClient`를 주입받아 프로필 이미지 조회
- 관련 파일:
  - module-auth/src/main/java/dev/emoforge/auth/service/MemberPublicProfileService.java
  - module-auth/src/main/java/dev/emoforge/auth/service/external/AttachClient.java
  - module-auth/src/main/java/dev/emoforge/auth/service/external/DummyAttachClient.java

대체 가능성:
- 높음(즉시 가능)
- 같은 모놀리식 DB/컨텍스트에서 `AttachmentRepository` 또는 `AttachmentService` 직접 조회로 대체 가능
- 이미 post 모듈은 `AttachmentRepository` 직접 참조 사례 존재:
  - module-post/src/main/java/dev/emoforge/post/service/query/PostQueryService.java

### B. post -> auth/attach (legacy bff)
현재:
- legacy facade가 `AuthClient`, `AttachClient` 주입
- 관련 파일:
  - module-post/src/main/java/dev/emoforge/post/service/legacy/bff/*
  - module-post/src/main/java/dev/emoforge/post/admin/service/bff/AdminPostListFacadeService.java

대체 가능성:
- 높음(대부분 이미 완료)
- 목록/상세/댓글 조회는 이미 내부 Query 서비스/Native Query로 대체 경로 존재
  - post 목록/상세: `PostQueryService` + `PostRepository` native query
  - 댓글 조회: `CommentQueryService` + `CommentRepository` native query(프로필 이미지 조인 포함)
  - 게시글 삭제: `PostService.deletePost()`에서 `attachmentRepository.deleteByPostId(postId)` 직접 수행

판단:
- post 쪽 Feign 기반 facade는 기능상 대체 가능하며 레거시 제거 대상.

---

## 4) 실제 사용 코드 vs 레거시 추정 코드

### 실제 사용(활성 경로)로 보이는 코드
1. auth 공개 프로필 API 체인
- module-auth/src/main/java/dev/emoforge/auth/controller/PublicProfileController.java
- module-auth/src/main/java/dev/emoforge/auth/service/MemberPublicProfileService.java
- 단, Attach 조회는 현재 Dummy 구현체가 우선될 가능성이 높아 실질 원격호출은 비활성 추정

2. post 현재 활성 조회/삭제 경로(Feign 미사용)
- module-post/src/main/java/dev/emoforge/post/service/query/PostQueryService.java
- module-post/src/main/java/dev/emoforge/post/service/query/CommentQueryService.java
- module-post/src/main/java/dev/emoforge/post/service/internal/PostService.java
- module-post/src/main/java/dev/emoforge/post/controller/PostController.java
- module-post/src/main/java/dev/emoforge/post/controller/CommentController.java

### 레거시로 추정되는 코드
1. legacy external clients
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/AuthClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/AttachClient.java

2. legacy bff facade
- module-post/src/main/java/dev/emoforge/post/service/legacy/bff/PostListFacadeService.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/bff/PostDetailFacadeService.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/bff/PostDeleteFacadeService.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/bff/CommentsFacadeService.java
- module-post/src/main/java/dev/emoforge/post/admin/service/bff/AdminPostListFacadeService.java

3. Dummy 구현체(Feign 제거 중 흔적)
- module-auth/src/main/java/dev/emoforge/auth/service/external/DummyAttachClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/DummyAuthClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/PostDummyAttachClient.java

4. Feign 설정/인터셉터
- module-post/src/main/java/dev/emoforge/post/config/FeignConfig.java
- module-post/src/main/java/dev/emoforge/post/config/FeignAuthInterceptor.java

근거 포인트:
- PostController 필드 주석에 `remove 대상` 명시
- CommentController에서 `CommentsFacadeService` 주입이 주석 처리됨
- Dummy 구현체에 `@Primary` 지정 및 "FeignClient를 걷어내는 중" 주석 존재

---

## 5) OpenFeign dependency 제거 시 영향 파일 목록

### 직접 컴파일 영향(반드시 수정 필요)
1. build.gradle
- module-auth/build.gradle (openfeign 의존 제거)
- module-post/build.gradle (openfeign 의존 제거)

2. Feign 타입 import/annotation 사용 Java
- module-auth/src/main/java/dev/emoforge/auth/service/external/AttachClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/AuthClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/AttachClient.java
- module-post/src/main/java/dev/emoforge/post/config/FeignConfig.java
- module-post/src/main/java/dev/emoforge/post/config/FeignAuthInterceptor.java

### 간접 영향(리팩토링 연쇄 가능)
- module-auth/src/main/java/dev/emoforge/auth/service/MemberPublicProfileService.java
- module-auth/src/main/java/dev/emoforge/auth/service/external/DummyAttachClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/DummyAuthClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/external/PostDummyAttachClient.java
- module-post/src/main/java/dev/emoforge/post/service/legacy/bff/*
- module-post/src/main/java/dev/emoforge/post/admin/service/bff/AdminPostListFacadeService.java
- emoforge-app/src/main/resources/application-*.yml 내 `service.attach.url` 설정(미사용 가능성 점검)

---

## 6) 안전한 제거를 위한 단계별 Migration Plan

### 단계 0. 사전 검증
1. `@EnableFeignClients` 부재 확인(완료)
2. 현재 API 동작 스모크 테스트(게시글 목록/상세/댓글/삭제, auth 공개프로필)
3. legacy facade 호출 경로가 실제 endpoint에서 사용되는지 재확인

### 단계 1. auth 경로 내부화
1. `MemberPublicProfileService`의 Attach 조회를 내부 서비스/리포지토리 호출로 전환
2. `DummyAttachClient` 제거 가능 상태 만들기
3. PublicProfile API 회귀 테스트

### 단계 2. post legacy 경로 정리
1. controller에서 legacy facade 필드/주석 제거
2. `legacy.external` + `legacy.bff`에서 실제 미사용 코드 제거
3. admin 경로에서 legacy facade 사용 여부 최종 확인 후 제거

### 단계 3. Feign 설정/의존 제거
1. `FeignConfig`, `FeignAuthInterceptor` 제거
2. `module-auth/build.gradle`, `module-post/build.gradle`에서 openfeign 의존 제거
3. 불필요 설정(`service.attach.url`, `service.auth.url`) 정리

### 단계 4. 검증
1. `./gradlew clean build` 전체 빌드
2. 통합 테스트/핵심 API 테스트
3. 로그 상 Feign 관련 Bean 생성/초기화 흔적이 없는지 확인

### 단계 5. 후속 정리
1. `legacy` 패키지명/주석 정리
2. 아키텍처 문서에 "모놀리식 내부 호출 원칙" 명시

---

## 리스크 메모
- auth 공개프로필에서 현재 Dummy 반환값(null) 기반으로 동작하고 있을 가능성이 있어, 내부화 시 NPE/예외 흐름을 명시적으로 정리해야 함.
- post legacy facade가 코드상 남아 있어도 실제 호출되지 않는 상태로 보이나, 스케줄러/내부 이벤트/관리자 경로에서 간접 호출이 없는지 최종 점검 필요.

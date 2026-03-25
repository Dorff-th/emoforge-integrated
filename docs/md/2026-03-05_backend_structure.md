# EMOFORGE Backend 구조 분석 (2026-03-05)

## 분석 범위
- 기준: 디렉토리/패키지 구조(`src/main/java`) 중심
- 제외: `build`, `out`, `.gradle`, `.idea` 등 빌드/IDE 산출물
- 코드 세부 로직은 읽지 않고, 패키지 명과 모듈 의존성으로 역할을 추론

## 1) 루트 디렉토리 구조

```text
emoforge-integrated-backend/
├─ emoforge-app
├─ emoforge-core
├─ module-auth
├─ module-post
├─ module-diary
├─ module-attachment
├─ gradle
├─ md
├─ build.gradle
├─ settings.gradle
└─ Dockerfile
```

## 2) 멀티모듈 구성 관점

`settings.gradle` 기준 활성 모듈:
- `emoforge-core`
- `emoforge-app`
- `module-auth`
- `module-post`
- `module-diary`
- `module-attachment`

의존 관계(요약):
- `emoforge-app`이 실행/조립 레이어 역할
  - `emoforge-core` + 모든 도메인 모듈(`auth/post/diary/attachment`) 의존
- 각 도메인 모듈은 공통적으로 `emoforge-core`에 의존
- `module-post`는 `module-attachment`도 함께 의존 (첨부 파일 연계)

## 3) 모듈별 패키지 구조와 역할 요약

### emoforge-app
주요 패키지:
- `dev.emoforge.app.common` (`exception`, `properties`, `response`)
- `dev.emoforge.app.debug`
- `dev.emoforge.app.security` (`endpoint`)

역할 요약:
- 스프링부트 엔트리포인트/애플리케이션 조립
- 공통 응답/예외/프로퍼티 관리
- 앱 레벨 보안 설정 및 엔드포인트 노출 조정

### emoforge-core
주요 패키지:
- `dev.emoforge.core.properties`
- `dev.emoforge.core.security` (`config`, `handler`, `jwt`, `principal`, `util`)

역할 요약:
- 전 모듈이 공유하는 핵심 인프라 제공
- 보안 코어(JWT, 시큐리티 설정/핸들러/Principal)와 공통 프로퍼티 제공

### module-auth
주요 패키지:
- `dev.emoforge.auth.config`
- `dev.emoforge.auth.controller` (`admin`)
- `dev.emoforge.auth.dto` (`admin`)
- `dev.emoforge.auth.entity`, `enums`, `repository`
- `dev.emoforge.auth.security` (`oauth`)
- `dev.emoforge.auth.infra` (`kakao`)
- `dev.emoforge.auth.service` (`admin`, `external`)
- `dev.emoforge.auth.token`

역할 요약:
- 인증/인가 도메인 전담 모듈
- OAuth 기반 소셜 연동(예: Kakao), 토큰 처리, 사용자 인증 API 제공
- 관리자 인증 관련 흐름 분리(`admin` 하위 패키지)

### module-post
주요 패키지:
- `dev.emoforge.post.config`
- `dev.emoforge.post.controller`
- `dev.emoforge.post.domain`, `repository`, `service`
- `dev.emoforge.post.dto` (`internal`, `query`, `legacy`, `legacy.bff`, `legacy.external`)
- `dev.emoforge.post.mapper`
- `dev.emoforge.post.admin` (`controller`, `dto`, `service`, `*.bff`)
- `dev.emoforge.post.util`

역할 요약:
- 게시글/피드 도메인 핵심 모듈
- 일반 사용자/관리자 기능 분리 및 조회/내부/레거시 DTO 계층 공존
- mapper 패키지 존재로 보아 SQL/조회 최적화 또는 MyBatis 연계 지점 포함

### module-diary
주요 패키지:
- `dev.emoforge.diary.config`
- `dev.emoforge.diary.controller`
- `dev.emoforge.diary.domain`, `repository`, `service`
- `dev.emoforge.diary.dto` (`request`, `response`, `page`, `statistics`, `music`)
- `dev.emoforge.diary.global` (`exception`)

역할 요약:
- 감정 일기(다이어리) 도메인 모듈
- CRUD + 페이징/통계/음악 연계 DTO가 분리된 구조
- 모듈 내부 글로벌 예외 처리 패키지 보유

### module-attachment
주요 패키지:
- `dev.emoforge.attach.config`
- `dev.emoforge.attach.controller`
- `dev.emoforge.attach.domain`, `repository`, `service`
- `dev.emoforge.attach.dto`
- `dev.emoforge.attach.policy`
- `dev.emoforge.attach.util`

역할 요약:
- 첨부파일 업로드/조회/관리 모듈
- 정책(`policy`) 패키지 분리로 파일 검증/규칙 관리 포인트가 명확
- `module-post`와의 결합으로 게시글 첨부 기능 지원

## 4) backend 모듈(전체)의 주요 역할 요약
- 멀티모듈 모놀리식 백엔드로, `app`이 실행을 담당하고 `core`가 공통 인프라를 제공
- 도메인 모듈(`auth`, `post`, `diary`, `attachment`)이 기능별로 수직 분리됨
- 패키지 공통 패턴(`controller`-`service`-`repository`-`dto`-`config`)이 반복되어 계층형 아키텍처가 일관됨
- 보안/인증(JWT/OAuth), 게시글, 다이어리, 첨부파일의 4개 핵심 비즈니스 축으로 구성됨

## 5) 관찰 메모
- `settings.gradle`에 `module-post`, `module-diary`, `module-attachment`가 중복 선언되어 있음(동작상 큰 문제는 없을 수 있으나 정리 권장).

# EMOFORGE Backend 아키텍처 개선 포인트 (2026-03-05)

## 분석 기준
- 디렉토리/패키지 구조 및 Gradle 모듈 의존성 기반 분석
- 비즈니스 코드 로직 세부 구현은 제외

## 현재 구조 한줄 요약
- `emoforge-app`(조립/실행) + `emoforge-core`(공통 보안/설정) + 도메인 모듈(`auth/post/diary/attachment`) 형태의 멀티모듈 모놀리식 구조

---

## 1) 모듈 분리 관점

### 현재 관찰
- 도메인 분리는 되어 있으나, 각 모듈 내부에 `controller/service/repository` 계층이 유사하게 반복됨.
- `module-post -> module-attachment` 직접 의존으로 도메인 간 결합이 존재.
- `settings.gradle`에 일부 모듈 중복 include(`module-post`, `module-diary`, `module-attachment`)가 있음.

### 개선 포인트
1. `settings.gradle` 중복 include 제거
- 빌드/설정 가독성 개선, 잠재적 혼선 제거.

2. 도메인 간 직접 의존 최소화
- `module-post`가 `module-attachment`를 직접 참조하기보다, 인터페이스 기반 포트(예: `AttachmentQueryPort`)로 의존하도록 전환.
- 구현체는 attachment 모듈에서 제공하고 app 레벨에서 wiring.

3. 모듈 경계 명확화(패키지 규칙)
- 외부 노출 패키지(`api`), 내부 구현(`internal`)을 분리해 모듈 내부 캡슐화 강화.
- 다른 모듈에서 `internal` 패키지 import 금지 규칙 도입.

4. 공통화 기준 재정의
- `emoforge-core`에는 "진짜 공통 인프라"만 유지.
- 도메인 특화 유틸/정책이 core로 유입되지 않도록 가드라인 문서화.

---

## 2) 의존 관계 관점

### 현재 관찰
- `emoforge-app`이 모든 도메인 모듈을 직접 조합하는 구조는 명확함.
- `emoforge-core`가 `api` 노출(`spring-security`, `validation`, `jjwt-api`, `openapi`)을 통해 하위 모듈에 강한 전파력을 가짐.
- 도메인 모듈들에서 보안 starter 중복 선언(`spring-boot-starter-security`)이 보임.

### 개선 포인트
1. 의존성 전파 축소 (`api` -> `implementation` 검토)
- core의 `api`는 꼭 필요한 타입만 남기고 나머지는 `implementation`으로 축소.
- 하위 모듈의 클래스패스 오염/결합도 감소.

2. 보안 의존성 단일화
- 보안은 core+app에서 일관 관리하고, 도메인 모듈의 중복 security starter 선언 제거 검토.
- 모듈별로 필요한 경우만 최소 의존 추가.

3. 데이터 접근 전략 정리
- JPA + MyBatis 혼용이 보이며 모듈별 기준이 불명확.
- 모듈 단위로 "주 저장소 기술"을 정의하고, 혼용 시 책임 분리 규칙(조회 전용 vs 트랜잭션 전용) 명문화.

4. 아키텍처 규칙 자동 검증 도입
- ArchUnit 또는 Gradle 기반 규칙 검사로 아래 자동화:
  - 도메인 간 금지 의존
  - controller -> repository 직접 접근 금지
  - internal 패키지 외부 참조 금지

---

## 3) 보안 구조 관점

### 현재 관찰
- `emoforge-core/security`에 JWT 필터/검증/핸들러/principal이 집중.
- `emoforge-app/security/SecurityConfig` + endpoint 그룹(`AuthEndpoints`, `PostEndpoints` 등)으로 인가 경로를 관리하는 형태.
- `module-auth`는 OAuth 사용자(`CustomOAuth2User`) 및 외부 연동(`infra/kakao`)을 담당.

### 개선 포인트
1. 인가 정책 중앙화 + 선언형 전환
- endpoint 목록 기반 permit/deny와 메서드 보안(`@PreAuthorize`)을 병행해 이중 안전장치 구성.
- 엔드포인트 클래스 증가 시 정책 누락 위험을 줄임.

2. 보안 경계 재정렬
- 인증/토큰 생성은 `module-auth`, 검증 필터/공통 핸들러는 `core`, 최종 체인 조립은 `app`으로 역할을 문서화.
- 현재 구조를 명문화해 신규 개발 시 경계 침범 방지.

3. OAuth 외부 연동 격리
- `infra/kakao`와 같은 외부 provider 연동 코드는 adapter 패턴으로 분리 유지.
- provider 추가 시 auth 서비스 코어 로직 변경 최소화.

4. 보안 테스트 계층 강화
- 필수 테스트 세트:
  - 공개/보호 엔드포인트 접근 제어 테스트
  - JWT 만료/위변조/권한 부족 케이스
  - OAuth 로그인 성공/실패/재시도 플로우

---

## 4) 유지보수 관점

### 현재 관찰
- 모듈/패키지 네이밍은 비교적 명확하지만, `legacy`, `external`, `internal`, `bff` 등이 증가해 구조 복잡도 상승 조짐.
- 중복 설정/중복 의존 선언이 장기적으로 관리 비용을 높일 수 있음.

### 개선 포인트
1. 패키지 수명주기 관리
- `legacy` 패키지에 deprecation 정책(삭제 목표 버전/날짜) 부여.
- 신규 기능은 legacy 경로 금지.

2. 모듈별 ADR(Architecture Decision Record) 추가
- 각 모듈에 "왜 이 의존/구조를 택했는지" 짧은 문서화.
- 팀 교체/확장 시 의사결정 비용 감소.

3. 공통 예외/응답 표준 통합
- app의 공통 응답/예외 정책과 모듈별 예외(`diary.global.exception` 등) 정합성 맞추기.
- 에러 코드 네이밍/HTTP 매핑 표준 표 형태로 관리.

4. 빌드/품질 게이트 정비
- CI에서 최소 게이트:
  - 모듈별 테스트
  - 아키텍처 규칙 검사
  - 의존성 중복/취약점 점검

---

## 우선순위 제안 (실행 순)
1. `settings.gradle` 중복 include 정리
2. 보안 의존성 중복 정리(core/app 중심으로 단일화)
3. `module-post -> module-attachment` 직접 의존을 포트/어댑터 방식으로 완화
4. 아키텍처 규칙 자동 검사(ArchUnit) 도입
5. legacy 패키지 정리 로드맵 수립

## 기대 효과
- 모듈 간 결합도 감소
- 보안 정책 누락 위험 감소
- 신규 기능 추가 시 영향 범위 축소
- 장기 유지보수 비용과 회귀 리스크 감소

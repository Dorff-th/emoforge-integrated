# Emoforge Integrated (Multi-Module)

기존 모놀리식MSA 구조의 단점을 보완하고, t2.micro 환경에서 효율적으로 구동하기 위해 재설계된 스프링 부트 멀티 모듈 프로젝트입니다.

## 🛠 Tech Stack
- Framework Spring Boot 3.3.4
- Language Java 17
- Build Tool Gradle (Multi-Module)
- Security Spring Security, JWT (Jakarta Servlet 기반)
- Data Spring Data JPA, MariaDB
- Communication Spring Cloud OpenFeign

## 📂 Module Structure
- emoforge-app 최상위 실행 모듈 (Main Class, 설정 통합)
- module-auth 인증 및 인가 로직 (SecurityConfig, MemberRepository)
- emoforge-core 공통 기술 부품 (JWT Provider, Common DTO, Exception)
- (추가 예정) module-post 게시판 및 커뮤니티 도메인
- (추가 예정) module-diary 감정 일기 도메인

## 🚀 Getting Started

### Prerequisites
- MariaDB 가동 중
- `service.attach.url` 등 외부 서비스 환경 변수 설정

### Build & Run
```bash
.gradlew clean build -x test
.gradlew emoforge-appbootRun
# Emoforge

Emoforge는 감정 기록과 게시판 기능을 결합한 개인 프로젝트입니다.  
초기에는 MSA(Microservice Architecture) 구조로 설계하였으며,  
이후 운영 복잡도와 리소스 효율을 고려하여 Monolith 구조로 통합하였습니다.

---

## 🧠 주요 기능

- 감정 기록 및 GPT 기반 피드백
- 게시판 (작성 / 수정 / 삭제 / 댓글 / 파일 첨부)
- 카카오 OAuth2 로그인
- JWT 기반 인증 / 인가 처리
- 통합 검색 기능

---

## 🏗 아키텍처

- Backend: Spring Boot
- Frontend: React (Vite + TypeScript)
- Infra: Docker Compose, GitHub Actions, AWS EC2
- DB: MySQL (RDS)

---

## 🔄 MSA → Monolith 전환 배경

초기에는 Cloud Native 구조 학습을 목적으로  
Auth, Post, Diary, LLM 서비스를 분리하여 설계하였습니다.

이후 운영 난이도 증가, 컨테이너 수 증가, 메모리 사용량 증가 등의  
현실적인 문제를 고려하여 단일 구조로 통합하였습니다.

이를 통해:

- 컨테이너 수 감소 (11 → 4)
- Swap 사용량 대폭 감소
- GitHub Actions 기반 CI/CD 구축
- 운영 복잡도 감소

---

## 🚀 실행 방법 (로컬)

```bash
# backend
cd emoforge-integrated-backend
./gradlew bootRun

# frontend
cd /emoforge-integrated-frontend
npm install
npm run dev
```

## 🚀 실행 방법 (Docker Compose 로컬)
```bash
docker-compose -f docker-compose.dev.yml up --build
```
---
## 📌 배포

- AWS EC2
- Docker Compose 기반 배포
- GitHub Actions 자동 빌드 및 배포

---

## 📄 Portfolio

아키텍처 전환 과정에 대한 상세 내용은  
`/about/portfolio` 페이지 및 PDF 문서를 참고하시기 바랍니다.
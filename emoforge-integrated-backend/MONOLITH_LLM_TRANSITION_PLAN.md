# MONOLITH LLM TRANSITION PLAN

작성일: 2026-02-14
목적: MSA의 langgraph_service를 Monolith 구조로 통합하면서
안정성을 유지하기 위한 단계적 변경 전략 정리

---

## 1. 현재 목표

- admin-fe 통합 완료
- Docker-compose로 Monolith + LLM 서비스 통합 예정
- 기능 안정화를 1차 목표로 함
- API prefix 변경은 보류

---

## 2. 서비스 명 변경

### 변경 사항
- AS-IS: langgraph_service
- TO-BE: llm_service

### 이유
- langgraph는 내부 구현 기술
- llm은 서비스 역할 기반 명칭
- 향후 내부 체인 구조 변경 대비

---

## 3. 변경 범위

### 변경하는 것

1. 디렉토리 이름
   langgraph_service → llm_service

2. docker-compose 서비스 이름
   langgraph_service → llm_service

3. FE 환경변수
   VITE_API_LANGGRAPH_BASE_URL → VITE_API_LLM_BASE_URL

4. vite-env.d.ts 타입 정의 수정

5. (선택) 로컬 도메인
   lang.127.0.0.1.nip.io → llm.127.0.0.1.nip.io

---

### 변경하지 않는 것 (중요)

1. FastAPI API prefix 유지
   /api/langgraph 그대로 유지

2. Spring 설정 base-url 유지
   http://lang.127.0.0.1.nip.io:8000/api/langgraph

3. LangGraphClient 클래스명 유지

---

## 4. 왜 API prefix를 유지하는가?

현재 단계는 "통합 안정화 단계"임.

API prefix 변경 시 영향 범위:

- Spring base-url 수정
- FastAPI router prefix 수정
- FE 호출 경로 수정
- Swagger 경로 변경
- 테스트 코드 수정
- 문서 수정

→ 테스트 범위 증가
→ Docker 통합 리스크 증가

따라서 prefix 변경은 2차 리팩토링 단계에서 진행.

---

## 5. 향후 2차 리팩토링 계획

Docker 통합 및 CI/CD 안정화 이후:

1. /api/langgraph → /api/llm 변경
2. LangGraphClient → LlmClient 변경
3. Spring config prefix 정리
4. FE env 정리
5. 관련 로그 및 문서 수정

이 작업은 별도 스프린트로 진행.

---

## 6. 현재 단계 우선순위

1. llm_service 단독 실행 테스트
2. FE 호출 정상 동작 확인
3. Docker-compose 통합
4. Monolith + LLM 동시 실행 검증
5. CI/CD 구성

---

## 7. 결론

현재는 기능 안정화 및 통합이 최우선.

구조 미학 및 네이밍 정리는
Docker 안정화 이후 도메인 단위 리팩토링으로 진행.

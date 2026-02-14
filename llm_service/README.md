# 🤖 **langgraph_service – GPT Orchestration Engine (FastAPI)**

*emoforge의 AI 기능(요약, 피드백, 음악추천)을 담당하는 FastAPI 기반 마이크로서비스*

---

`langgraph_service`는 emoforge 플랫폼의 **AI 처리 전용 엔진**입니다.

FastAPI와 LangGraph 기반으로 GPT 흐름을 제어하며, diary-service에서 요청하는

요약/피드백/음악추천 기능을 실제로 처리합니다.

---

# 📌 1. 서비스 개요

`langgraph_service`는 다음 기능을 제공합니다:

- GPT 기반 감정 요약(summary)
- GPT 기반 감정 피드백(feedback)
- 감정 기반 음악 추천 (YouTube Music 검색 기반)
- GPT 프롬프트 조합 및 컨텍스트 구성
- diary-service의 “AI 로직”을 전담하는 마이크로서비스 역할

**핵심 포지션**

> diary-service는 “일기 저장/조회” 중심.
> 
> 
> langgraph_service는 “AI 생성/분석” 중심.
> 

둘이 역할이 명확히 분리됨.

---

# 🧠 2. 주요 기술 스택

### Backend

- **FastAPI**
- **LangGraph**
- **Pydantic**
- **Uvicorn**

### AI

- **OpenAI API (gpt-4o-mini → 향후 교체 예정)**
- YouTube Music 검색 로직

### Infra

- Docker / Docker Compose
- AWS EC2 (내부 네트워크로만 접근)

---

# 🗂️ 3. 디렉토리 구조

```
langgraph_service/
 ├─ app/
 │   ├─ api/
 │   │   ├─ summary.py            # GPT 요약
 │   │   ├─ feedback.py           # GPT 피드백
 │   │   ├─ music.py              # 음악 추천
 │   │   └─ health.py             # 헬스체크
 │   ├─ graphs/
 │   │   ├─ summary_graph.py      # LangGraph summary logic
 │   │   ├─ feedback_graph.py     # LangGraph feedback logic
 │   │   └─ music_graph.py        # LangGraph emotion→music
 │   ├─ models/                   # Request/Response pydantic
 │   ├─ services/                 # GPT 호출/유틸
 │   └─ core/                     # config, settings
 ├─ Dockerfile
 └─ README.md  ← (본 문서)

```

---

# 🔧 4. 주요 API

### 📘 1) GPT 요약

```
POST /api/summary

```

입력:

- content
- emotion
- feeling_ko

출력:

- summary
- feedback (선택)

---

### 📘 2) GPT 피드백

```
POST /api/feedback

```

입력:

- emotion
- feeling_ko or content

출력:

- 분석된 한줄 감정 피드백

---

### 📘 3) 감정 기반 음악 추천

```
POST /api/music

```

출력 예:

```json
{
  "title": "Rainy Days",
  "videoUrl": "https://music.youtube.com/watch?v=XXXX",
  "emotionScore": 3
}

```

---

### 📘 4) Health Check

```
GET /health

```

---

# 🧩 5. diary-service와의 연동 구조

AI 흐름은 다음과 같음:

```
diary-frontend
      ↓
diary-service (Spring Boot)
      ↓ REST API (internal network)
langgraph_service (FastAPI)
      ↓
OpenAI GPT
      ↓
결과를 diary-service에서 DB 저장

```

역할 분리가 명확:

- **LangGraph** = GPT 처리 + 프롬프트 엔진
- **diary-service** = 비즈니스 로직 + 저장

---

# ⚙️ 6. 실행 & 배포

### Docker 빌드

```
sudo docker-compose -f docker-compose.backend.prod.yml build langgraph_service

```

### 실행

```
sudo docker-compose -f docker-compose.backend.prod.yml --env-file .env.prod up -d langgraph_service

```

### 로그 확인

```
sudo docker logs -f langgraph_service

```

---

# 🔐 7. 환경 변수(.env.prod 예시)

```
OPENAI_API_KEY=xxxx
OPENAI_MODEL=gpt-4o-mini

YOUTUBE_MUSIC_COOKIE=xxxx   # 필요 시
LANGCHAIN_TRACING=false

```

---

# 🤝 8. 주요 LangGraph 노드 설명

### 📘 summary_graph.py

- 일기 내용(content)을 요약
- 감정(emotion)에 맞춰 톤 조절
- 한글 감정(feeling_ko)을 반영

### 📘 feedback_graph.py

- ‘오늘 기분’ 한마디 → 영어 감정표현 생성
- 격려/피드백 톤 선택 가능

### 📘 music_graph.py

- emotion score → 음악 분위기 분석
- YouTube Music에서 트랙 추천

---

# ⚠️ 9. 주의사항

- GPT-4o-mini 모델은 OpenAI에서 서비스종료 예정 → 교체 준비 필요
- API 응답 속도는 diary-service보다 느릴 수 있음
- EC2 t2.micro 환경에서는 CPU spikes 발생 가능
- GPT 호출 실패 시 diary-service는 fallback 처리 필요
- 음악추천 기능은 YouTube Music 구조 변경 시 API 수정 필요

---

# 🧱 10. 향후 개선 계획

- GPT-4.1-mini 또는 4.1-omni 기반 재구성
- 감정 기반 주간/월간 summary 자동 생성
- 노드형 LangGraph → workflow 구조 upgrade
- 감정 프로파일링(Emotion Profile AI) 기능 도입
- 음악 추천 캐싱
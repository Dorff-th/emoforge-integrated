---

# 📁 2️⃣ backend/README.md

```md
# Emoforge Backend

Spring Boot 기반 REST API 서버입니다.

---

## ⚙ 기술 스택

- Java 21
- Spring Boot
- Spring Security (JWT)
- JPA + MyBatis
- MySQL
- Docker

---

## 📦 주요 모듈

- auth
- post
- diary
- attachment
- core

---

## 🔐 인증 구조

- OAuth2 (Kakao)
- JWT Access / Refresh Token
- Role 기반 권한 분리 (USER / ADMIN)

---

## 🚀 실행

```bash
./gradlew bootRun
```
또는 Docker:
```bash
docker build -t emoforge-backend .
docker run -p 8081:8081 emoforge-backend
```
---
## 🌍 환경 변수

DB_URL

DB_USERNAME

DB_PASSWORD

JWT_SECRET

KAKAO_CLIENT_ID

KAKAO_REDIRECT_URI


---
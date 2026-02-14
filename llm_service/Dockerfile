# langgraph_service/Dockerfile
FROM python:3.10-slim

WORKDIR /app

# requirements.txt 복사 및 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 소스 전체 복사
COPY . .

# FastAPI 포트
EXPOSE 8000

# 기본 실행 명령
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000", "--reload"]

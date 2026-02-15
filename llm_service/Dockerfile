FROM python:3.11-slim

WORKDIR /app

# requirements 복사
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# 전체 소스 복사
COPY . .

EXPOSE 8000

# 🔥 app.py 안에 app = FastAPI() 라고 가정
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]

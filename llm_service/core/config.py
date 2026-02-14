from pydantic_settings import BaseSettings
from typing import List
import os


class Settings(BaseSettings):
    APP_ENV: str
    APP_NAME: str
    APP_HOST: str
    APP_PORT: int
    BASE_URL: str

    # 🔐 인증 관련
    JWT_SECRET: str
    JWT_ALGORITHM: str
    API_KEY: str

    # 🧠 OpenAI
    OPENAI_API_KEY: str
    OPENAI_MODEL: str

    # 🎵 YouTube API
    YOUTUBE_API_KEY: str
    YOUTUBE_BASE_URL: str
    YOUTUBE_WATCH_URL: str
    YOUTUBE_MUSIC_URL: str

    # 🌍 CORS
    CORS_ALLOW_ORIGINS: List[str] = []

    # 🪵 Logging
    LOG_LEVEL: str
    LOG_FILE: str

    class Config:
        # ✅ APP_ENV 값(dev/prod)에 따라 자동으로 다른 env 파일 로드
        env_file = (
            ".env.prod"
            if os.getenv("APP_ENV") == "prod"
            else ".env.dev"
        )
        env_file_encoding = "utf-8"  # 윈도우 환경에서 깨짐 방지

# ✅ Settings 인스턴스 생성
settings = Settings()

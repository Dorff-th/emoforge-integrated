from pydantic import BaseModel
from typing import List


class MusicRecommendRequest(BaseModel):
    emotionScore: float
    feelingKo: str
    diaryContent: str
    artistPreferences: List[str]


class MusicRecommendResponseItem(BaseModel):
    title: str
    channel: str
    url: str
    thumbnail: str


class MusicRecommendResponse(BaseModel):
    query: str
    total: int
    results: List[MusicRecommendResponseItem]

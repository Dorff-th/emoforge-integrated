# ✅ routes/music_router.py
from fastapi import Request
from fastapi import APIRouter, HTTPException
from schemas.music_schema import MusicRecommendRequest, MusicRecommendResponse
from services.music_service import recommend_music_simple   # ✅ 변경됨

router = APIRouter(prefix="/api/langgraph/diary/gpt/music", tags=["Music Recommendation"])


# ✅ FastAPI router
@router.post("/recommendations/simple")
async def recommend_music(musicRecommendRequest: MusicRecommendRequest, request: Request):

    print("🔥 라우터 진입 성공")

    source = request.headers.get("X-Source", "UNKNOWN")

    result = await recommend_music_simple(
        emotionScore=musicRecommendRequest.emotionScore,
        feelingKo=musicRecommendRequest.feelingKo,
        diaryContent=musicRecommendRequest.diaryContent,
        artist_preferences=musicRecommendRequest.artistPreferences,
        source=source
    )
    print("✅ 결과 생성 완료")
    return result

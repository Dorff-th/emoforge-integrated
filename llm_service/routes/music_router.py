# ✅ routes/music_router.py
from fastapi import APIRouter, HTTPException
from schemas.music_schema import MusicRecommendRequest, MusicRecommendResponse
from services.music_service import recommend_music_simple   # ✅ 변경됨

router = APIRouter(prefix="/api/langgraph/diary/gpt/music", tags=["Music Recommendation"])


# ✅ FastAPI router
@router.post("/recommendations/simple")
async def recommend_music(request: MusicRecommendRequest):
    print("🔥 라우터 진입 성공")
    result = await recommend_music_simple(
        emotionScore=request.emotionScore,
        feelingKo=request.feelingKo,
        diaryContent=request.diaryContent,
        artist_preferences=request.artistPreferences
    )
    print("✅ 결과 생성 완료")
    return result

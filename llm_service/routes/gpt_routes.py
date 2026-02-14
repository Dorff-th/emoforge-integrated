from fastapi import APIRouter, Depends
from pydantic import BaseModel, Field
from typing import List, Optional
from core.auth_dependency import verify_jwt_from_cookie, require_role
from services.gpt_summary_service import summarize_diary
from services.gpt_feedback_service import generate_feedback
from services.gpt_feeling_service import translate_feeling

#router = APIRouter(prefix="/api/diary/gpt", tags=["GPT Diary"])
router = APIRouter(prefix="/api/langgraph/diary/gpt", tags=["GPT Diary"])

# ✅ 감정 영어 표현
class FeelingRequest(BaseModel):
    feelingKo: str

@router.post("/feeling")
async def get_feeling(req: FeelingRequest):
    return await translate_feeling(req)

# NOTE:
# - This endpoint is intentionally NOT protected by JWT.
# - Authentication is handled by Spring Boot.
# - LangGraph is treated as an internal AI service.
# - Gateway pattern may be introduced in production phase.


# ✅ 감정·습관·회고 피드백
class FeedbackRequest(BaseModel):
    uuid: str = Field(..., description="member uuid (from Spring / FE)")
    emotionScore: int = Field(..., description="감정 점수 (1~5)")
    habitTags: Optional[List[str]] = Field(default_factory=list)
    feelingKo: Optional[str] = ""
    feelingEn: Optional[str] = ""
    diaryContent: str
    feedbackStyle: str = "encourage"

# @router.post("/feedback")
# async def get_feedback(req: FeedbackRequest, user=Depends(verify_jwt_from_cookie)):
#     return await generate_feedback(req, user)

@router.post("/feedback")
async def get_feedback(req: FeedbackRequest):
    return await generate_feedback(req)


# ✅ 회고 요약
class SummaryRequest(BaseModel):
    date: str
    content: str

@router.post("/summary")
async def get_summary(req: SummaryRequest):
    return await summarize_diary(req)



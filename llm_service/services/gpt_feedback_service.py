import asyncio
from graph_feedback import compiled_feedback_graph

#AS-IS
async def generate_feedback(req, user):
    """
    emotionScore + habitTags + feelingKo + feelingEn + diaryContent + feedbackStyle 기반으로
    LangGraph 피드백 생성. (스레드 분리로 비동기 블로킹 방지)
    """
    member_uuid = user.get("member_uuid") or user.get("uuid")

    async def _run_graph():
        return await asyncio.to_thread(lambda: compiled_feedback_graph.invoke({
            "emotionScore": req.emotionScore,
            "habitTags": req.habitTags,
            "feelingKo": req.feelingKo,
            "feelingEn": req.feelingEn,
            "diaryContent": req.diaryContent,
            "feedbackStyle": req.feedbackStyle
        }))

    result = await _run_graph()

    return {
        "member_uuid": member_uuid,
        "feedback": result["result"]
    }

#TO-BE
async def generate_feedback(req):
    """
    emotionScore + habitTags + feelingKo + feelingEn + diaryContent + feedbackStyle 기반으로
    LangGraph 피드백 생성. (스레드 분리로 비동기 블로킹 방지)
    """
    member_uuid = req.uuid;


    async def _run_graph():
        return await asyncio.to_thread(lambda: compiled_feedback_graph.invoke({
            "emotionScore": req.emotionScore,
            "habitTags": req.habitTags,
            "feelingKo": req.feelingKo,
            "feelingEn": req.feelingEn,
            "diaryContent": req.diaryContent,
            "feedbackStyle": req.feedbackStyle
        }))

    result = await _run_graph()

    return {
        "member_uuid": member_uuid,
        "feedback": result["result"]
    }

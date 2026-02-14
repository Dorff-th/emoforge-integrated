import logging
import time
import asyncio
from graph_feedback import compiled_feedback_graph



#TO-BE

logger = logging.getLogger("llm")
logging.basicConfig(level=logging.INFO)

async def generate_feedback(req, source):
    member_uuid = req.uuid
    
    start_time = time.time()

    try:
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

        duration = round(time.time() - start_time, 3)

        logger.info(
            f"[LLM] type=feedback source={source} member={member_uuid} "
            f"status=SUCCESS duration={duration}s"
        )

        return {
            "member_uuid": member_uuid,
            "feedback": result["result"]
        }

    except Exception as e:
        duration = round(time.time() - start_time, 3)

        logger.error(
            f"[LLM] type=feedback source={source} member={member_uuid} "
            f"status=ERROR duration={duration}s error={str(e)}",
            exc_info=True
        )
        raise

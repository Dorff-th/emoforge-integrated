import logging
import time
import asyncio
from graph_summary import compiled_summary_graph

logger = logging.getLogger("llm")
logging.basicConfig(level=logging.INFO)

async def summarize_diary(req, source):
    """
    날짜(date) + 회고 내용(content)을 받아 LangGraph로 하루 요약 생성.
    LangGraph 내부 LLM 호출은 동기식이므로 별도 스레드로 실행.
    """

    start_time = time.time()

    try :
        async def _run_graph():
            return await asyncio.to_thread(lambda: compiled_summary_graph.invoke({
                "date": req.date,
                "content": req.content
            }))
        

        result = await _run_graph()

        duration = round(time.time() - start_time, 3)

        logger.info(
            f"[LLM] type=gpt_summary source={source} "
            f"status=SUCCESS duration={duration}s"
        )

        return {"summary": result["result"]}
    
    except Exception as e:
        duration = round(time.time() - start_time, 3)

        logger.error(
            f"[LLM] type=gpt_summary source={source}  "
            f"status=ERROR duration={duration}s error={str(e)}",
            exc_info=True
        )
        raise

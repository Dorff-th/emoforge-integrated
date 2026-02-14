import logging
import time
import asyncio
from graph_feeling import compiled_graph

logger = logging.getLogger("llm")
logging.basicConfig(level=logging.INFO)

async def translate_feeling(req, source):
    start_time = time.time()

    try:
        async def _run_graph():
            return await asyncio.to_thread(
                lambda: compiled_graph.invoke({
                    "feelingKo": req.feelingKo
                })
            )

        result = await _run_graph()

        duration = round(time.time() - start_time, 3)

        logger.info(
            f"[LLM] type=feeling source={source} "
            f"status=SUCCESS duration={duration}s"
        )

        # LangGraph 응답 파싱
        lines = [line.strip() for line in result["result"].split("\n") if line.strip()]
        return {"suggestions": lines}

    except Exception as e:
        duration = round(time.time() - start_time, 3)

        logger.error(
            f"[LLM] type=feeling source={source}  "
            f"status=ERROR duration={duration}s error={str(e)}",
            exc_info=True
        )
        raise

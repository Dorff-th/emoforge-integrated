import asyncio
from graph_feeling import compiled_graph


async def translate_feeling(req):
    """
    사용자의 감정(feelingKo)을 LangGraph를 통해 영어 표현으로 변환.
    내부 LangChain LLM은 동기 함수이므로 별도 스레드에서 실행한다.
    """
    async def _run_graph():
        return await asyncio.to_thread(lambda: compiled_graph.invoke({"feelingKo": req.feelingKo}))

    result = await _run_graph()

    # LangGraph 응답 파싱
    lines = [line.strip() for line in result["result"].split("\n") if line.strip()]
    return {"suggestions": lines}

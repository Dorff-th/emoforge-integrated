import asyncio
from graph_summary import compiled_summary_graph


async def summarize_diary(req):
    """
    날짜(date) + 회고 내용(content)을 받아 LangGraph로 하루 요약 생성.
    LangGraph 내부 LLM 호출은 동기식이므로 별도 스레드로 실행.
    """
    async def _run_graph():
        return await asyncio.to_thread(lambda: compiled_summary_graph.invoke({
            "date": req.date,
            "content": req.content
        }))

    result = await _run_graph()
    return {"summary": result["result"]}

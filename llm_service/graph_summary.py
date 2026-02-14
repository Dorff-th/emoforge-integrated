# graph_summary.py
import os
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain.schema import SystemMessage, HumanMessage
from langgraph.graph import StateGraph, START, END

load_dotenv()

llm = ChatOpenAI(
    model=os.getenv("OPENAI_MODEL", "gpt-4o-mini"),
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.6  # 약간 더 차분한 톤 유지
)

# 상태 정의
class SummaryState(dict):
    date: str
    content: str
    result: str

# GPT 요약 노드
def summarize_day(state: SummaryState) -> SummaryState:
    date = state["date"]
    content = state["content"]

    prompt = f"""
    당신은 사용자의 회고 내용을 간결하고 따뜻하게 요약하는 한국어 요약 전문가입니다.
    아래는 {date}에 작성한 감정일기 피드백입니다. 전체 내용을 한두 문단으로 요약해 주세요.

    {content}
    """

    response = llm.invoke([
        SystemMessage(content="당신은 사용자의 회고 내용을 따뜻하고 공감 있게 요약하는 전문가입니다."),
        HumanMessage(content=prompt)
    ])
    state["result"] = response.content.strip()
    return state

# 그래프 구성
graph = StateGraph(SummaryState)
graph.add_node("summarize_day", summarize_day)
graph.add_edge(START, "summarize_day")
graph.add_edge("summarize_day", END)

compiled_summary_graph = graph.compile()

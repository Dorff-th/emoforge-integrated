# graph_feeling.py
import os
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain.schema import SystemMessage, HumanMessage
from langgraph.graph import StateGraph, START, END

# 환경 변수 로드
load_dotenv()

# LLM 초기화
llm = ChatOpenAI(
    model=os.getenv("OPENAI_MODEL", "gpt-4o-mini"),
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.7
)

# ✅ 상태(state) 구조 정의
# LangGraph 0.6.9에서는 state를 명시적으로 정의해야 함
class FeelingState(dict):
    feelingKo: str
    result: str

# ✅ 감정 → 영어 문장 변환 노드
def translate_feeling(state: FeelingState) -> FeelingState:
    feelingKo = state["feelingKo"]
    prompt = f"""
    사용자가 "{feelingKo}" 라고 느꼈을 때,
    이를 짧고 일상적인 영어 문장으로 자연스럽게 표현한 예시를 3~5개 추천해줘.
    각 문장은 따로 줄바꿈해서 보여줘.
    """
    response = llm.invoke([
        SystemMessage(content="You are an expert in expressing Korean emotions in natural English sentences."),
        HumanMessage(content=prompt)
    ])
    state["result"] = response.content
    return state

# ✅ 그래프 정의
graph = StateGraph(FeelingState)

# 노드 등록
graph.add_node("translator", translate_feeling)

# 흐름 정의 (시작 → translator → 끝)
graph.add_edge(START, "translator")
graph.add_edge("translator", END)

# ✅ 컴파일 (필수)
compiled_graph = graph.compile()

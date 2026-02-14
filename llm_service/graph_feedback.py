# graph_feedback.py (개선본)
import os
import random
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain.schema import SystemMessage, HumanMessage
from langgraph.graph import StateGraph, START, END

load_dotenv()

llm = ChatOpenAI(
    model=os.getenv("OPENAI_MODEL", "gpt-4o-mini"),
    api_key=os.getenv("OPENAI_API_KEY"),
    temperature=0.7
)

# 🧩 상태 정의 (입력 파라미터 확장)
class FeedbackState(dict):
    emotionScore: int
    habitTags: list
    feelingKo: str
    feelingEn: str
    diaryContent: str
    feedbackStyle: str
    stylePrompt: str
    result: dict  # JSON 구조로 반환 예정

# 🎨 스타일 선택 노드
def select_style(state: FeedbackState) -> FeedbackState:
    style = state.get("feedbackStyle", "encourage")

    styles = {
        "encourage": "따뜻하고 긍정적이며 응원하는 말투로, 감정을 공감하며 위로해주세요.",
        "scold": "너는 사용자의 행동을 냉정하게 짚는 현실적인 조언자다. 불필요한 위로나 공감은 하지 말고, 사용자가 회피하고 있는 지점과 개선이 필요한 행동을 직설적으로 짚어라. 인격을 비난하지 말고, 선택과 행동만을 기준으로 말해라.",
        "roast": "유머를 사용하되, 사용자의 회피나 자기합리화를 그대로 드러내어 찔러라. 행동을 정당화하거나 변호하는 표현은 사용하지 마라. 웃기게 말해도 메시지는 분명해야 하며, 듣는 사람이 잠깐 불편함을 느껴도 괜찮다. 인격을 비하하지 말고, 행동과 태도, 반복되는 패턴만을 소재로 삼아라.",
        "coach": "냉정하지만 진심 어린 코치처럼 목표 중심의 조언을 해주세요.",
        "random": random.choice([
            "따뜻하고 긍정적이며 응원하는 말투로, 감정을 공감하며 위로해주세요.",
            "단호하지만 애정 어린 말투로, 개선할 점을 명확히 짚어주세요.",
            "유머러스하고 재치있게, 가볍게 놀리며 개선점을 알려주세요.",
            "냉정하지만 진심 어린 코치처럼 목표 중심의 조언을 해주세요."
        ]),
        "default": "무난한 말투로 간결하게 답변해주세요."
    }

    state["stylePrompt"] = styles.get(style, styles["encourage"])
    return state


# 🧠 GPT 피드백 생성 노드
def generate_feedback(state: FeedbackState) -> FeedbackState:
    emotion = state["emotionScore"]
    habits = ", ".join(state["habitTags"]) if state["habitTags"] else "없음"
    feelingKo = state["feelingKo"]
    feelingEn = state["feelingEn"]
    content = state["diaryContent"]
    stylePrompt = state["stylePrompt"]

    prompt = f"""
    아래의 감정 및 회고 데이터를 기반으로 {stylePrompt} 피드백을 생성해주세요.

    [감정 점수] {emotion}/5
    [완료한 습관] {habits}
    [오늘의 한마디] {feelingKo} ({feelingEn})
    [회고 내용] {content}

    출력은 반드시 아래 JSON 형식으로 해주세요:
    {{
      "summary": "오늘 하루를 한 문장으로 요약",
      "encouragement": "감정 기반 격려 문장",
      "next_tip": "내일을 위한 제안 한 문장"
    }}
    """

    response = llm.invoke([
        SystemMessage(content="You are an empathetic AI coach generating feedback based on the user's mood and habits."),
        HumanMessage(content=prompt)
    ])

    # LLM의 응답 파싱
    try:
        import json
        state["result"] = json.loads(response.content)
    except Exception:
        # 파싱 실패 시 문자열 통째로 저장
        state["result"] = {"summary": "", "encouragement": response.content.strip(), "next_tip": ""}

    return state


# ⚙️ LangGraph 구성
graph = StateGraph(FeedbackState)
graph.add_node("select_style", select_style)
graph.add_node("generate_feedback", generate_feedback)
graph.add_edge(START, "select_style")
graph.add_edge("select_style", "generate_feedback")
graph.add_edge("generate_feedback", END)

compiled_feedback_graph = graph.compile()

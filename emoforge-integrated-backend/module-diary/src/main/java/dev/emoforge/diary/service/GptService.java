package dev.emoforge.diary.service;


import dev.emoforge.diary.domain.DiaryEntry;
import dev.emoforge.diary.domain.GptSummary;
import dev.emoforge.diary.global.exception.DataNotFoundException;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.GptSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GptService {

    private final DiaryGptClient diaryGptClient;
    private final DiaryEntryRepository diaryEntryRepository;
    private final GptSummaryRepository gptSummaryRepository;
    private final LangGraphClient langGraphClient; // ✅ 새로 주입되는 클라이언트

    public String generateAndSaveSummary(String memberUuid, LocalDate date) {
        // 1. 해당 날짜의 회고들 가져오기
        List<DiaryEntry> diaryList = diaryEntryRepository
                .findByMemberUuidAndDiaryDate(memberUuid, date);

        if (diaryList.isEmpty()) {
            throw new RuntimeException("회고가 없습니다.");
        }

        // 2. 회고 내용을 하나로 합치기
        String fullContent = diaryList.stream()
                .map(DiaryEntry::getContent)
                .collect(Collectors.joining("\n"));

        // 3. LangGraph-Service에 요약 요청
        try {
            String summary = langGraphClient.requestSummary(date, fullContent);

            // 4. DB 저장
            GptSummary entity = new GptSummary();
            entity.setMemberUuid(memberUuid);
            entity.setDiaryDate(date);
            entity.setSummary(summary);
            gptSummaryRepository.save(entity);

            log.info("✅ LangGraph 요약 저장 완료: {}", summary);
            return summary;

        } catch (Exception e) {
            log.error("❌ LangGraph 요약 요청 실패", e);
            throw new RuntimeException("LangGraph-Service 요약 요청 실패", e);
        }
    }



    @Transactional
    public String generateSummary(String memberUuid) {
        LocalDate today = LocalDate.now();

        DiaryEntry diary = diaryEntryRepository.findTopByMemberUuidAndDiaryDateOrderByCreatedAtDesc(memberUuid, today)
                .orElseThrow(() -> new DataNotFoundException("오늘 회고가 없습니다."));

        String prompt = buildSummaryPrompt(diary);

        // 1. GPT 호출
        String gptResult = diaryGptClient.getSummaryFromGpt(prompt);

        // 2. GptSummary 저장 (기존 요약 있으면 업데이트)
        GptSummary summary = gptSummaryRepository.findByDiaryEntry(diary)
                .orElse(new GptSummary());

        summary.setDiaryEntry(diary);
        summary.setSummary(gptResult);

        gptSummaryRepository.save(summary);
        return gptResult;
    }

    @Transactional
    public String generateFeedback(String memberUuid) {
        LocalDate today = LocalDate.now();

        DiaryEntry diary = diaryEntryRepository.findTopByMemberUuidAndDiaryDateOrderByCreatedAtDesc(memberUuid, today)
                .orElseThrow(() -> new DataNotFoundException("오늘 회고가 없습니다."));

        String prompt = buildFeedbackPrompt(diary);

        String gptFeedback = diaryGptClient.getFeedbackFromGpt(prompt);

        diary.setFeedback(gptFeedback);
        diaryEntryRepository.save(diary);

        return gptFeedback;
    }

    private String buildSummaryPrompt(DiaryEntry diary) {
        return String.format(
                "다음은 하루의 감정일기입니다:\n\n%s\n\n이를 한 문단으로 요약해 주세요. 핵심 감정과 주요 사건을 중심으로 부드럽게 정리해 주세요.",
                diary.getContent()
        );
    }

    private String buildFeedbackPrompt(DiaryEntry diary) {
        String habits = String.join(", ", diary.getHabitTags());

        return String.format("""
        오늘 사용자가 작성한 회고입니다:
        
        감정 점수: %d
        오늘의 기분 한마디: %s
        오늘 실천한 습관: %s
        감정일기: %s
        
        이 내용을 바탕으로 사용자를 격려하거나 동기를 줄 수 있는 짧은 피드백 메시지를 작성해 주세요. 
        너무 길지 않고 따뜻한 느낌으로 부탁합니다.
        """,
                diary.getEmotion(),
                diary.getFeelingKo(),
                habits,
                diary.getContent()
        );
    }

}

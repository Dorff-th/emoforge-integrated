package dev.emoforge.diary.service;


import dev.emoforge.diary.domain.DiaryEntry;
import dev.emoforge.diary.domain.GptSummary;
import dev.emoforge.diary.dto.response.GPTSummaryResponseDTO;
import dev.emoforge.diary.dto.response.SummaryResponseDTO;
import dev.emoforge.diary.global.exception.DataNotFoundException;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.GptSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final GptSummaryRepository gptSummaryRepository;

    // 사용자의 오늘의 회고 요약인데 지금은 오늘의 회고중 최신 데이터 1개만 가져옴
    public SummaryResponseDTO getTodaySummary(String memberUuid) {
        LocalDate today = LocalDate.now();

        DiaryEntry diary = diaryEntryRepository.findTopByMemberUuidAndDiaryDateOrderByCreatedAtDesc(memberUuid, today)
                .orElseThrow(() -> new DataNotFoundException("오늘의 회고가 없습니다."));

        GptSummary gpt = gptSummaryRepository.findByDiaryEntry(diary).orElse(null);

        return new SummaryResponseDTO(
                diary.getDiaryDate(),
                diary.getEmotion(),
                diary.getFeelingKo(),
                diary.getFeelingEn(),
                diary.getHabitTags(),
                diary.getContent(),
                gpt != null ? gpt.getSummary() : null,
                diary.getFeedback()
        );
    }

    public GPTSummaryResponseDTO getTodayGPTSummary(String memberUuid) {

        LocalDate today = LocalDate.now();
        GptSummary todayGptSummary = gptSummaryRepository.findByMemberUuidAndDiaryDate(memberUuid, today)
                .orElseThrow(() -> new DataNotFoundException("오늘의 GPT 요약이 없습니다."));

        return new GPTSummaryResponseDTO(todayGptSummary.getSummary());

    }
}


package dev.emoforge.diary.service;

import dev.emoforge.diary.dto.response.MemberDiaryStatsResponse;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.GptSummaryRepository;
import dev.emoforge.diary.repository.MusicRecommendHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 특정 회원의 감정·회고 활동 통계를 집계하는 서비스.
 * - DiaryEntry, GptSummary, MusicRecommendHistory 등의 개수를 조회하여
 *   사용자 프로필 화면 등에서 활용할 수 있는 요약 데이터를 제공한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryActivityStatsService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final GptSummaryRepository gptSummaryRepository;
    private final MusicRecommendHistoryRepository musicRecommendHistoryRepository;

    public MemberDiaryStatsResponse getUserDiaryStats(String memberUuid) {
        int diaryEntryCount = diaryEntryRepository.countByMemberUuid(memberUuid);
        int gptSummaryCount = gptSummaryRepository.countByMemberUuid(memberUuid);
        int musicRecommendHistoryCount = musicRecommendHistoryRepository.countByMemberUuid(memberUuid);

        MemberDiaryStatsResponse response = MemberDiaryStatsResponse.builder()
                .diaryEntryCount(diaryEntryCount)
                .gptSummaryCount(gptSummaryCount)
                .musicRecommendHistoryCount(musicRecommendHistoryCount)
                .build();

        return response;
    }

}

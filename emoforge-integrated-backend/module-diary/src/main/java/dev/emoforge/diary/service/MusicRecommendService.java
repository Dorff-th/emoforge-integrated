package dev.emoforge.diary.service;

import dev.emoforge.diary.domain.DiaryEntry;
import dev.emoforge.diary.domain.MemberArtistPreference;
import dev.emoforge.diary.domain.MusicRecommendHistory;
import dev.emoforge.diary.domain.MusicRecommendSong;
import dev.emoforge.diary.dto.music.LangGraphRequest;
import dev.emoforge.diary.dto.music.LangGraphResponse;
import dev.emoforge.diary.dto.music.MusicRecommendHistoryDTO;
import dev.emoforge.diary.dto.music.RecommendResultDTO;
import dev.emoforge.diary.global.exception.DataNotFoundException;
import dev.emoforge.diary.repository.DiaryEntryRepository;
import dev.emoforge.diary.repository.MemberArtistPreferenceRepository;
import dev.emoforge.diary.repository.MusicRecommendHistoryRepository;
import dev.emoforge.diary.repository.MusicRecommendSongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicRecommendService {

    private final DiaryEntryRepository diaryEntryRepo;
    private final MusicRecommendHistoryRepository historyRepo;
    private final MusicRecommendSongRepository songRepo;
    private final MemberArtistPreferenceRepository artistPrefRepo;
    private final LangGraphClient langGraphClient; // ✅ 새로 주입되는 클라이언트

    /**
     * LangGraph-Service를 호출해 DiaryEntry의 감정 데이터를 기반으로 음악 추천을 생성·저장한다.
     */
    @Transactional
    public RecommendResultDTO recommendForDiaryEntry(Long diaryEntryId, List<String> artistPrefs, String memberUuid) {

        // 1) 입력 수집
        DiaryEntry entry = diaryEntryRepo.findById(diaryEntryId)
                .orElseThrow(() -> new DataNotFoundException("DiaryEntry not found"));

        // 2) LangGraph 호출 (B2B)
        LangGraphRequest req = LangGraphRequest.builder()
                .emotionScore(entry.getEmotion())     // int
                .feelingKo(entry.getFeelingKo())      // String
                .content(entry.getContent())          // String
                .artistPreferences(artistPrefs)       // List<String>
                .build();

        LangGraphResponse res = langGraphClient.requestMusicRecommendations(req); // 타임아웃/예외 처리 내장

        // ✅ 추천 결과 DTO로 변환
        RecommendResultDTO dto = RecommendResultDTO.from(res);

        // ✅ 추천 결과가 없거나 "No results found"일 경우 DB 저장 생략
        if (dto.getSongs() == null
                || dto.getSongs().isEmpty()
                || (dto.getSongs().size() == 1
                && "No results found".equalsIgnoreCase(dto.getSongs().get(0).getTitle()))) {

            log.warn("추천 음악이 없어 DB 저장을 생략합니다. diaryEntryId={}", diaryEntryId);

            // ✅ keyword는 유지, songs만 비운 상태로 반환
            return RecommendResultDTO.builder()
                    .keyword(dto.getKeyword())
                    .songs(Collections.emptyList())
                    .build();
        }

        // 3) DB 저장 (history + songs)
        MusicRecommendHistory history = historyRepo.save(
                MusicRecommendHistory.builder()
                        .diaryEntry(entry)
                        .memberUuid(memberUuid)
                        .emotionScore(entry.getEmotion())
                        .feelingKo(entry.getFeelingKo())
                        .content(entry.getContent())
                        .keywordSummary(res.getKeyword())
                        .build()
        );

        List<MusicRecommendSong> songs = res.getRecommendations().stream()
                .map(song -> songRepo.save(MusicRecommendSong.builder()
                        .history(history)
                        .artistName(song.getArtist())
                        .songTitle(song.getTitle())
                        .youtubeUrl(song.getUrl())
                        .thumbnailUrl(song.getThumbnail())
                        .build()))
                .toList();


        // 4) member_artist_preference 업데이트
        updateMemberArtistPreferences(memberUuid, artistPrefs, songs);

        return RecommendResultDTO.from(res);

    }

    /**
     * 추천된 음악 목록 조회
     */
    @Transactional(readOnly = true)
    public MusicRecommendHistoryDTO getRecommendationsForDiary(Long diaryEntryId) {
        MusicRecommendHistory history = historyRepo.findWithSongsByDiaryEntryId(diaryEntryId)
                .orElseThrow(() -> new DataNotFoundException("No recommendations found for this diary entry"));

        return MusicRecommendHistoryDTO.fromEntity(history);
    }

    /**
     * 사용자의 아티스트 선호도 누적 업데이트 (recommendForDiaryEntry 메서드에서 사용)
     */
    private void updateMemberArtistPreferences(String memberUuid, List<String> usedArtists, List<MusicRecommendSong> songs) {

        Set<String> allArtists = new HashSet<>();

        // (1) 직접 입력한 아티스트
        if (usedArtists != null) {
            usedArtists.forEach(a -> allArtists.add(normalizeArtistName(a)));
        }

        // (2) 추천 결과 아티스트
        songs.stream()
                .map(MusicRecommendSong::getArtistName)
                .map(this::normalizeArtistName)
                .forEach(allArtists::add);

        // (3) 선호도 업데이트
        for (String artistName : allArtists) {
            boolean isUserInput = usedArtists != null && usedArtists.stream()
                    .map(this::normalizeArtistName)
                    .anyMatch(a -> a.equalsIgnoreCase(artistName));

            int increment = isUserInput ? 2 : 1; // 사용자 입력 가중치 +2, 추천은 +1

            artistPrefRepo.findByMemberUuidAndArtistName(memberUuid, artistName)
                    .ifPresentOrElse(existing -> {
                        existing.setPreferenceScore(existing.getPreferenceScore() + increment);
                        existing.setUpdatedAt(LocalDateTime.now());
                    }, () -> {
                        artistPrefRepo.save(MemberArtistPreference.builder()
                                .memberUuid(memberUuid)
                                .artistName(artistName)
                                .preferenceScore((double) increment)
                                .updatedAt(LocalDateTime.now())
                                .build());
                    });
        }
    }

    /**
     * 아티스트 이름 표준화 (updateMemberArtistPreferences 메서드에서 사용): "Baby V.O.X - Topic" → "Baby V.O.X"
     */
    private String normalizeArtistName(String artistRaw) {
        if (artistRaw == null) return "";
        String name = artistRaw.trim();
        // 예: " - Topic" 제거
        if (name.contains("-")) {
            name = name.substring(0, name.indexOf("-")).trim();
        }
        // 공백 정리
        return name.replaceAll("\\s+", " ");
    }
}

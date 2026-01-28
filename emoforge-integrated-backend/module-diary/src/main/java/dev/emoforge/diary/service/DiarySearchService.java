package dev.emoforge.diary.service;

import dev.emoforge.diary.dto.request.DiarySearchRequestDTO;
import dev.emoforge.diary.dto.response.DiarySearchResultDTO;
import dev.emoforge.diary.repository.DiaryEntrySearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiarySearchService {

    private final DiaryEntrySearchRepository diaryEntrySearchRepository;

    public Page<DiarySearchResultDTO> search(String memberUuid, DiarySearchRequestDTO request, Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "diaryDate")
        );

        String query = request.getQuery();
        List<String> fields = request.getFields();
        //Long memberId = request.getMemberId();

        Map<String, Integer> emotionMap = request.getEmotionMap();
        Map<String, LocalDate> diaryDateMap = request.getDiaryDateMap();

        return diaryEntrySearchRepository.searchDiaries(query, fields, memberUuid, emotionMap, diaryDateMap, sortedPageable);
    }
}

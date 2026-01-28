package dev.emoforge.diary.repository;


import dev.emoforge.diary.dto.response.DiarySearchResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DiaryEntrySearchRepository {
    Page<DiarySearchResultDTO> searchDiaries(String query,
                                             List<String> fields,
                                             String memberUuid,
                                             Map<String, Integer> emotionMap,
                                             Map<String, LocalDate> dateMap,
                                             Pageable pageable);
}

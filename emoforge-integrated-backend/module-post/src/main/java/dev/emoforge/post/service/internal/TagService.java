package dev.emoforge.post.service.internal;


import dev.emoforge.post.domain.Tag;
import dev.emoforge.post.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 자동완성 태그 목록 조회
     * @param query 사용자가 입력한 검색어
     * @return 태그 이름 목록 (최대 20개)
     */
    public List<String> getTagSuggestions(String query) {
        // 1) 방어 코드: query가 null이거나 너무 짧으면 빈 리스트
        if (query == null || query.trim().length() < 2) {
            return List.of(); // 2글자 이상일 때만 검색
        }

        // 2) 앞뒤 공백 제거
        String normalized = query.trim();

        // 3) DB 검색 (Repository에서 대소문자 무시)
        List<String> results = tagRepository.findByNameStartingWithIgnoreCase(normalized);

        // 4) 최대 20개 제한
        if (results.size() > 20) {
            return results.subList(0, 20);
        }
        return results;
    }

    public Tag getOrCreateTag(String name) {
        // 공백 제거 + 소문자 변환 (normalize)
        String normalized = name.trim();

        return tagRepository.findByNameIgnoreCase(normalized)
                .orElseGet(() -> tagRepository.save(
                        Tag.builder().name(normalized).build()
                ));
    }
}
